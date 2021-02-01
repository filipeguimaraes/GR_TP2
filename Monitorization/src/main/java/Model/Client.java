package Model;

import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Classe de comunicação para o SNMP usando o SNMP4j.
 * Adaptado de <a href="https://blog.jayway.com/author/johanrask/">Johan Rask</a> como
 * forma de descobrir as ferramentas que o SNMP4j disponibiliza.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A85308
 * @see <a href="https://blog.jayway.com/2010/05/21/introduction-to-snmp4j/">Introdution to SNMP4J</a>
 */
public class Client {
    // Endereço IP
    private final String address;
    // Community String
    private final String community;
    // Objeto SNMP
    private Snmp snmp;

    /**
     * Construtor da classe.
     *
     * @param address   Endereço a monitorizar. (Endereço+"/"+Port)
     * @param community String community
     * @throws IOException Não consegue começar como endereço e o string fornecida.
     */
    public Client(String address, String community) throws IOException {
        this.address = address;
        this.community = community;
        start();
    }

    // **************************************************
    // Métodos Privados
    // **************************************************

    /**
     * Método que coloca o programa a "ouvir" SNMP no endereço e porta fornecidas.
     *
     * @throws IOException Caso o endereço ou porta estejam errados.
     */
    private void start() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    /**
     * Método que cria o target dando duas tentativas, um período de timeout de 1500 ms,
     * e obrigando o uso da versão 2c do SNMP como solicitado no enunciado.
     *
     * @return O target do SNMP4J para monitorização do endereço.
     */
    private Target<Address> getTarget() {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget<Address> target = new CommunityTarget<>(targetAddress, new OctetString(this.community));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    /**
     * Método que cria uma lista de treeEvents que permitem percorrer uma coluna de um determinado OID.
     *
     * @param OID OID da coluna pretendida.
     * @return Lista de treeEvents da coluna pretendida
     */
    private List<TreeEvent> getColumn(String OID) {
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        return treeUtils.getSubtree(getTarget(), new OID(OID));
    }

    // **************************************************
    // Métodos Públicos
    // **************************************************

    /**
     * Método que "inicializa" um mapa de processos em que a chave é o pid do processo
     * e preenche já com o PID e nome correspondente.
     *
     * @param OID OID da coluna com o nome dos processos
     * @return Mapa com processos mas ainda sem ram e cpu
     * @throws IOException Caso não consiga aceder à coluna
     */
    public Map<Integer, Process> getName(String OID) throws IOException {
        List<TreeEvent> eventos = getColumn(OID);

        if (eventos == null || eventos.isEmpty()) {
            throw new IOException("Não conseguiu aceder ao valor solicitado");
        }

        Map<Integer, Process> processos = new TreeMap<>();
        for (TreeEvent event : eventos) {
            if (event != null) {
                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    continue;
                }
                for (VariableBinding varBinding : varBindings) {
                    if (varBinding != null) {
                        processos.put(varBinding.getOid().last(),
                                new Process(varBinding.getOid(), varBinding.getVariable().toString()));
                    }
                }
            }
        }

        return processos;
    }

    /*
     * Método que adiciona a memoria de cada processo
     */

    /**
     * Método que adiciona a memoria a cada processo.
     *
     * @param OID         OID da coluna com a ram.
     * @param processos   Mapa de processos criado por {@link #getName(String)}.
     * @param totalMemory Memoria total no sistema.
     * @throws IOException Caso não consiga aceder à coluna.
     */
    public void getMem(String OID, Map<Integer, Process> processos, Integer totalMemory) throws IOException {
        List<TreeEvent> eventos = getColumn(OID);
        if (eventos == null || eventos.isEmpty()) {
            throw new IOException("Erro ao carregar a memória");
        }

        for (TreeEvent event : eventos) {
            if (event != null) {
                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    continue;
                }
                for (VariableBinding varBinding : varBindings) {
                    if (varBinding != null) {
                        processos.get(varBinding.getOid().last())
                                .setMem(varBinding.getVariable().toInt(), totalMemory);
                    }
                }
            }
        }
    }

    /**
     * Método que adiciona o cpu a cada processo.
     *
     * @param OID       OID da coluna com o uso do cpu por processo.
     * @param processos Mapa de processos criado por {@link #getName(String)}.
     * @throws IOException Caso não consiga aceder à coluna.
     */
    public void getCPU(String OID, Map<Integer, Process> processos) throws IOException {
        List<TreeEvent> eventos = getColumn(OID);
        if (eventos == null || eventos.isEmpty()) {
            throw new IOException("Erro ao carregar o cpu");
        }

        for (TreeEvent event : eventos) {
            if (event != null) {
                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    continue;
                }
                for (VariableBinding varBinding : varBindings) {
                    if (varBinding != null) {
                        processos.get(varBinding.getOid().last())
                                .setCentiseconds(varBinding.getVariable().toInt());
                    }
                }
            }
        }
    }

    /**
     * Método usado para obter um valor no formato String usando a primitiva SNMP GET.
     *
     * @param oid OID do valor a solicitar.
     * @return String com o valor pretendido.
     * @throws IOException Caso não consiga aceder ao valor
     */
    public String getString(String oid) throws IOException {
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);
        ResponseEvent<Address> event = snmp.send(pdu, getTarget(), null);

        if (event == null) {
            throw new IOException("Erro ao obter String");
        }

        return event.getResponse().get(0).getVariable().toString();
    }

    /**
     * Método que termina a conexão.
     *
     * @throws IOException Caso não tenha sucesso a terminar.
     */
    public void stop() throws IOException {
        snmp.close();
    }


}
