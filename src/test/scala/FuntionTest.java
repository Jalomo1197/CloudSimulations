import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;



public class FuntionTest {
    CreateCustomDataCenters simBuilder = new CreateCustomDataCenters();
    Config config = ConfigFactory.load("config.conf");
    long CLOUDLET_FILE_SIZE = Long.parseLong(config.getString("jdbc.CLOUDLET_FILE_SIZE"));
    long CLOUDLET_OUTPUT_SIZE = Long.parseLong(config.getString("jdbc.CLOUDLET_OUTPUT_SIZE"));
    long CLOUDLET_MIN_LENGTH = Long.parseLong(config.getString("jdbc.CLOUDLET_MIN_LENGTH"));
    long CLOUDLET_MAX_LENGTH = Long.parseLong(config.getString("jdbc.CLOUDLET_MAX_LENGTH"));

    @Test
    public void testRandomIntFunction() {
        int inRange;
        for (int i = 0; i < 10; i++){
            inRange = CreateCustomDataCenters.getRandomNumberUsingInts(1, 1000);
            boolean range = (inRange >= 1 && inRange <= 1000);
            assertTrue(range);
        }
    }

    @Test
    public void testCloudCreationFileSizes(){
        Cloudlet c = simBuilder.createCloudlet(100);
        assertEquals(CLOUDLET_FILE_SIZE, c.getFileSize());
        assertEquals(CLOUDLET_OUTPUT_SIZE,c.getOutputSize());
    }

    @Test
    public void testMinLengthCloudletCreations(){
        List<Cloudlet> MinLengthCloudlet = simBuilder.createCloudlets("CLOUDLET_MIN_LENGTH");
        MinLengthCloudlet.forEach(c -> {
            assertEquals(CLOUDLET_MIN_LENGTH, c.getLength());
        });
    }

    @Test
    public void testMaxLengthCloudletCreations(){
        List<Cloudlet> MinLengthCloudlet = simBuilder.createCloudlets("CLOUDLET_MAX_LENGTH");
        MinLengthCloudlet.forEach(c -> {
            assertEquals(CLOUDLET_MAX_LENGTH, c.getLength());
        });
    }


}
