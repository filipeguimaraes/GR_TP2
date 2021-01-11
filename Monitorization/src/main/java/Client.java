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
 * Simplest client possible
 *
 * @author johanrask
 */
public class Client {

    private final String address;
    private String community;
    private Snmp snmp;


    public Client(String address,String community) throws IOException {
        super();
        this.address = address;
        this.community = community;
        start();
    }


    private void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public String getString(String oid) throws IOException {
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);

        if (event == null) {
            throw new RuntimeException("Nenhum evento");
        }

        return event.getResponse().get(0).getVariable().toString();
    }


    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget(targetAddress, new OctetString(this.community));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }


    public List<TreeEvent> getColumn(String OID) {
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        return treeUtils.getSubtree(getTarget(), new OID(OID));
    }


    /*
     * Método que *inicializa* um map de processos em que a chave é o pid é e coloca o pid e nome respetivo
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
                        processos.put(varBinding.getOid().last(), new Process(varBinding.getOid(), varBinding.getVariable().toString()));
                    }
                }
            }
        }

        return processos;
    }

    /*
     * Método que adiciona a memoria de cada processo
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
                                .setMem(varBinding.getVariable().toInt(),totalMemory);
                    }
                }
            }
        }
    }

    /*
     * Método que adiciona o cpu de cada processo
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
                                .setCpu(varBinding.getVariable().toInt());
                    }
                }
            }
        }
    }


    // Ends the conection
    public void stop() throws IOException {
        snmp.close();
    }


}
