import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyFirstFit;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisioner;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.builders.tables.TextTableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.text.DecimalFormat;
import java.util.*;


public class CreateCustomDataCenters {
    Config config = ConfigFactory.load("config.conf");
    Integer HOSTS = Integer.parseInt(config.getString("jdbc.HOSTS"));
    Integer HOST_PES = Integer.parseInt(config.getString("jdbc.HOST_PES"));
    Integer HOST_PES_MIPS = Integer.parseInt(config.getString("jdbc.HOST_PES_MIPS"));
    Integer HOST_RAM = Integer.parseInt(config.getString("jdbc.HOST_RAM"));
    Integer HOST_BW = Integer.parseInt(config.getString("jdbc.HOST_BW"));
    Integer HOST_STORAGE = Integer.parseInt(config.getString("jdbc.HOST_STORAGE"));
    Integer VMS = Integer.parseInt(config.getString("jdbc.VMS"));
    Integer VM_MIPS = Integer.parseInt(config.getString("jdbc.VM_MIPS"));
    Integer VM_RAM = Integer.parseInt(config.getString("jdbc.VM_RAM"));
    Integer VM_BW = Integer.parseInt(config.getString("jdbc.VM_BW"));
    Integer VM_PES = Integer.parseInt(config.getString("jdbc.VM_PES"));
    Integer VM_SIZE = Integer.parseInt(config.getString("jdbc.VM_SIZE"));
    Integer CLOUDLETS = Integer.parseInt(config.getString("jdbc.CLOUDLETS"));
    Integer CLOUDLET_PES = Integer.parseInt(config.getString("jdbc.CLOUDLET_PES"));
    Integer CLOUDLET_FILE_SIZE = Integer.parseInt(config.getString("jdbc.CLOUDLET_FILE_SIZE"));
    Integer CLOUDLET_OUTPUT_SIZE = Integer.parseInt(config.getString("jdbc.CLOUDLET_OUTPUT_SIZE"));
    Integer CLOUDLET_MIN_LENGTH = Integer.parseInt(config.getString("jdbc.CLOUDLET_MIN_LENGTH"));
    Integer CLOUDLET_MAX_LENGTH = Integer.parseInt(config.getString("jdbc.CLOUDLET_MAX_LENGTH"));
    Double MEM_COST = Double.parseDouble(config.getString("jdbc.MEM_COST"));
    Double BW_COST = Double.parseDouble(config.getString("jdbc.BW_COST"));
    Double STORAGE_COST = Double.parseDouble(config.getString("jdbc.STORAGE_COST"));
    Double SECOND_COST = Double.parseDouble(config.getString("jdbc.SECOND_COST"));
    Logger logger;

    private final CloudSim simulation;
    private final CloudSim simulation2;
    private final CloudSim simulation3;

    public CreateCustomDataCenters() {
        logger = LoggerFactory.getLogger(CreateCustomDataCenters.class);
        logger.info("Beginning of Creating Simulations");

        this.simulation = new CloudSim();
        DatacenterBroker broker = createBrokers(simulation);
        List<Cloudlet> cloudletList = createCloudlets("CLOUDLET_DYNAMIC_LENGTH");
        Datacenter datacenter1 = createDatacenter(new VmSchedulerSpaceShared(), this.simulation);
        List<Vm> vm_SpaceShared = createVms(new CloudletSchedulerSpaceShared());
        broker.submitVmList(vm_SpaceShared);
        broker.submitCloudletList(cloudletList);
        logger.info("Start DataCenter One Simulation");
        simulation.start();
        logger.info("End DataCenter One Simulation");
        final List<Cloudlet> finishedCloudlets =  broker.getCloudletFinishedList();
        addStatColumns(new CloudletsTableBuilder(finishedCloudlets)).build();
        Stats d1_stats = new Stats(finishedCloudlets).buildStatTable();


        this.simulation2 = new CloudSim();
        DatacenterBroker broker2 = createBrokers(simulation2);
        List<Cloudlet> cloudletList2 = createCloudlets("CLOUDLET_DYNAMIC_LENGTH");
        Datacenter datacenter2 = createDatacenter(new VmSchedulerTimeShared(), this.simulation2);
        List<Vm> vm_TimeShared = createVms(new CloudletSchedulerTimeShared());
        broker2.submitVmList(vm_TimeShared);
        broker2.submitCloudletList(cloudletList2);
        logger.info("Start DataCenter Two Simulation");
        simulation2.start();
        logger.info("End DataCenter Three Simulation");
        final List<Cloudlet> finishedCloudlets2 =  broker2.getCloudletFinishedList();
        addStatColumns(new CloudletsTableBuilder(finishedCloudlets2)).build();
        Stats d2_stats = new Stats(finishedCloudlets2).buildStatTable();


        this.simulation3 = new CloudSim();
        DatacenterBroker broker3 = createBrokers(simulation3);
        List<Cloudlet> cloudletList3 = createCloudlets("CLOUDLET_DYNAMIC_LENGTH");
        Datacenter datacenter3 = createDatacenter(new VmSchedulerTimeShared(), this.simulation3);
        List<Vm> vm_CompletelyFair = createVms(new CloudletSchedulerCompletelyFair());
        broker3.submitVmList(vm_CompletelyFair);
        broker3.submitCloudletList(cloudletList3);
        logger.info("Start DataCenter Three Simulation");
        simulation3.start();
        logger.info("Start DataCenter Three Simulation");
        final List<Cloudlet> finishedCloudlets3 =  broker3.getCloudletFinishedList();
        addStatColumns(new CloudletsTableBuilder(finishedCloudlets3)).build();
        Stats d3_stats = new Stats(finishedCloudlets3).buildStatTable();
// new CloudletsTableBuilder(finishedCloudlets).addColumn()

    }


    private CloudletsTableBuilder addStatColumns(CloudletsTableBuilder table){
        table.addColumn(new TextTableColumn("CloudLet","CPU Time"), cloudlet -> new DecimalFormat("#.000").format(cloudlet.getActualCpuTime()));
        table.addColumn(new TextTableColumn("CloudLet","File Size"), cloudlet -> cloudlet.getFileSize());
        table.addColumn(new TextTableColumn("CloudLet Cost","BW Cost"), cloudlet -> cloudlet.getAccumulatedBwCost());
        table.addColumn(new TextTableColumn("CloudLet Cost","Processing Cost"), cloudlet ->  new DecimalFormat("#.000").format(cloudlet.getCostPerSec() * cloudlet.getActualCpuTime()));
        table.addColumn(new TextTableColumn("CloudLet Cost","Total Cost"), cloudlet ->  new DecimalFormat("#.000").format(cloudlet.getTotalCost()));
        return table;
    }


    // Creates a broker that is linked to a specified simulation
    private DatacenterBroker createBrokers(CloudSim simulation) {
        DatacenterBroker broker = new DatacenterBrokerSimple(simulation);
        broker.setVmDestructionDelayFunction(vm -> 10.0);
        logger.info("Broker created with vmDestructionDelay: 10 seconds & Broker Implementation: " + broker.getClass().getName());
        return broker;
    }


    /**
     * Creates a Datacenter and its Hosts. The amount of host on a data center is specified in the config file (HOSTS)
     */
    private Datacenter createDatacenter(VmScheduler HOST_VmScheduler, final CloudSim simulation) {
        final List<Host> hostList = new ArrayList<>(HOSTS);

        for(int i = 0; i < HOSTS; i++) {
            Host host = createHost(HOST_VmScheduler);
            hostList.add(host);
        }
        logger.info("Created hosts for data center. Host amount: " + HOSTS +", Processing elements per host: "+ HOST_PES + ", Vm scheduler policy: " + HOST_VmScheduler.getClass().getName());
        final Datacenter dc = new DatacenterSimple(simulation, hostList, new VmAllocationPolicyFirstFit());
        dc.setSchedulingInterval(5); //delay in some, important
        // Cost for each element are determined through the values in the config file.
        dc.getCharacteristics().setCostPerBw(BW_COST);
        dc.getCharacteristics().setCostPerMem(MEM_COST);
        dc.getCharacteristics().setCostPerSecond(SECOND_COST);
        dc.getCharacteristics().setCostPerStorage(STORAGE_COST);

        logger.info("Created data center. Cost Per BW: "+ BW_COST +", Cost Per Mem: "+ MEM_COST +", Cost Per Second: " + SECOND_COST +", Cost Per Storage: "+ SECOND_COST);
        return dc;
    }

    /**
     * Creates HOST and its list of processing elements (PE). The amount of processing elements on a host is specified in the config file (HOST_PES)
     */
    private Host createHost(VmScheduler HOST_VmScheduler) {
        List<Pe> peList = new ArrayList<>(HOST_PES);
        //List of Host's CPUs (Processing Elements, PEs)
        for (int i = 0; i < HOST_PES; i++) {
            peList.add(new PeSimple(HOST_PES_MIPS, new PeProvisionerSimple()));
        }

        Host host = new HostSimple(HOST_RAM, HOST_BW, HOST_STORAGE, peList);
        host
                .setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(HOST_VmScheduler);

        return host;
    }

    /**
     * Creates Virtue Machines and sets the properties of each VM. these properties are specified in the config file
     * (VM_MIPS, VM_PES, VM_RAM, VM_BW, VM_SIZE)
     */
    private List<Vm> createVms(CloudletScheduler VM_CloudletScheduler) {
        final List<Vm> list = new ArrayList<>(VMS);
        for (int i = 0; i < VMS; i++) {
            //Uses a CloudletSchedulerTimeShared by default to schedule Cloudlets
            final Vm vm = new VmSimple(VM_MIPS, VM_PES);
            vm.setRam(VM_RAM).setBw(VM_BW).setSize(VM_SIZE).setCloudletScheduler(VM_CloudletScheduler);
            list.add(vm);
        }
        logger.info("Created virtual machines. VM MIPS: " + VM_MIPS +", VM processing elements: "+ VM_PES +", VM RAM: "+ VM_RAM +", VM BW: "+ VM_BW +", VM size: " + VM_SIZE);
        return list;
    }

    /**
     * Creates Cloudlets and sets the properties of each application. these properties are specified in the config file
     * (VM_MIPS, VM_PES, VM_RAM, VM_BW, VM_SIZE)
     */
    private List<Cloudlet> createCloudlets(String Type_of_Cloudlets){
        final List<Cloudlet> cloudlets = new ArrayList<>();
        Map<Long, Integer> CloudletExecTime_N = new TreeMap<Long, Integer>();
        long length = (Type_of_Cloudlets.equals("CLOUDLET_MAX_LENGTH"))? CLOUDLET_MAX_LENGTH : ((Type_of_Cloudlets.equals("CLOUDLET_MIN_LENGTH"))? CLOUDLET_MIN_LENGTH : 1000);


        if(Type_of_Cloudlets.equals("CLOUDLET_MAX_LENGTH") || Type_of_Cloudlets.equals("CLOUDLET_MIN_LENGTH")){
            logger.info("Creating Cloudlets (applications) with fixed length (MI). length: " + length);
            for (int i = 1; i <= CLOUDLETS; i++) {
                Cloudlet cloudlet = createCloudlet(length);
                cloudlet.setFileSize(CLOUDLET_FILE_SIZE);
                cloudlet.setOutputSize(CLOUDLET_OUTPUT_SIZE);
                cloudlets.add(createCloudlet(length));
            }

        }
        /*  O(n)
        *   In the scenario that the cloudlets' length is to be dynamic, A Cloudlet list with ascending length (MI) values
        *   is returned (with priority values set). This is done in case that the Vms are made with their CloudletScheduler
        *   set to CloudletSchedulerCompletelyFair, where CloudletSchedulerCompletelyFair takes into account the cloudlets'
        *   priority value to computes process timeslice based on its weight.
        * */
        else if(Type_of_Cloudlets.equals("CLOUDLET_DYNAMIC_LENGTH")){
            logger.info("Creating Cloudlets (applications) with dynamic lengths (MI). Range of lengths: (" + CLOUDLET_MIN_LENGTH +", " + CLOUDLET_MAX_LENGTH + ")");
            for (int i = 1; i <= CLOUDLETS; i++) {
                length = getRandomNumberUsingInts(CLOUDLET_MIN_LENGTH, CLOUDLET_MAX_LENGTH + 1);
                if(CloudletExecTime_N.containsKey(length))
                    CloudletExecTime_N.put(length, CloudletExecTime_N.get(length) + 1);
                else
                    CloudletExecTime_N.put(length, 1);
            }

            for (Map.Entry<Long, Integer> entry : CloudletExecTime_N.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++){
                    Cloudlet cloudlet = createCloudlet(entry.getKey());
                    cloudlet.setPriority(i);
                    cloudlet.setFileSize(CLOUDLET_FILE_SIZE);
                    cloudlet.setOutputSize(CLOUDLET_OUTPUT_SIZE);
                    cloudlets.add(cloudlet);
                }
            }
            logger.info("Priorities set for each cloudlet based on length relative to others (MI).");
        }
        else{
            logger.warn("Cloudlet construction type was invalid, 0 cloudlets were made. Must be of type (CLOUDLET_DYNAMIC_LENGTH, CLOUDLET_MAX_LENGTH, CLOUDLET_MIN_LENGTH).");
        }

        logger.info("Created Cloudlets (applications). File size: " + CLOUDLET_FILE_SIZE + ", Output file size: " + CLOUDLET_OUTPUT_SIZE);
        return cloudlets;
    }

    // Creates CloudletSimple with UtilizationModel:UtilizationModelFull
    private Cloudlet createCloudlet( final long length) {
       // UtilizationModel utilization = new UtilizationModelFull(); // TODO: inspect and modify
        final Cloudlet cloudlet = new CloudletSimple(length, CLOUDLET_PES);
        cloudlet
                .setFileSize(CLOUDLET_FILE_SIZE)
                .setOutputSize(CLOUDLET_OUTPUT_SIZE);
              //  .setUtilizationModel(utilization);
        return cloudlet;
    }

    /*
       Random int generator: Used to assign different Cloudlet lengths (MI) to simulate different branches taken
       in a Cloudlet execution
    */
    private int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max).findFirst().getAsInt();
    }


    private static class Stats{
        Config config = ConfigFactory.load("config.conf");
        private final List<Double> APPLICATION_MI = config.getDoubleList("jdbc.APPLICATION_MI");
        private final List<Double> APPLICATION_RUN_PRICE =  config.getDoubleList("jdbc.APPLICATION_RUN_PRICE");
        Integer PRICE_LEVELS = Integer.parseInt(config.getString("jdbc.PRICING_LEVELS"));
        private Double TOTAL_PRICE = 0.0;
        private Double TOTAL_COST_OF_DATABASE_MODEL = 0.0;
        private final Double PROFIT;
        private final Double PROFIT_RATE;
        private final Double cloudletsRan;

        public Stats(List<Cloudlet> finishedCloudlets){
            cloudletsRan = Double.parseDouble(Integer.toString(finishedCloudlets.size()));
            finishedCloudlets.forEach(c -> {
                TOTAL_COST_OF_DATABASE_MODEL += c.getTotalCost();
                double cludlet_MI = Double.parseDouble(Long.toString(c.getLength()));
                for(int i = 0; i < PRICE_LEVELS; i++){
                    //Double priceSection =
                    if(cludlet_MI <= APPLICATION_MI.get(i))
                        TOTAL_PRICE += APPLICATION_RUN_PRICE.get(i);
                }
                if(cludlet_MI > APPLICATION_MI.get(PRICE_LEVELS - 1))
                    TOTAL_PRICE += APPLICATION_RUN_PRICE.get(PRICE_LEVELS - 1);
            });

            PROFIT = TOTAL_PRICE - TOTAL_COST_OF_DATABASE_MODEL;
            PROFIT_RATE = PROFIT / TOTAL_COST_OF_DATABASE_MODEL;
        }

        public Stats buildStatTable(){
            System.out.println("\n\nAverage Price Per Application| Income Of Running Cloutlets | Expenses Of Data Center Resources | Profit       | Profit Rate");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
            System.out.print( new DecimalFormat("#0.000000000000               ").format(TOTAL_PRICE/cloudletsRan));
            System.out.print( "|");
            System.out.print( new DecimalFormat("#0.000000000000            ").format(TOTAL_PRICE));
            System.out.print( "|");
            System.out.print( new DecimalFormat("#0.000000000000                   ").format(TOTAL_COST_OF_DATABASE_MODEL));
            System.out.print( "|");
            System.out.print( new DecimalFormat("#0.0000000000").format(PROFIT));
            System.out.print( "|");
            System.out.println( new DecimalFormat("#0.000000000000").format(PROFIT_RATE) + "%");

            return this;
        }
    }

}
