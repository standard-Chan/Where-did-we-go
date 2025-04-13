import com.wheredidwego.MyApplication;
import com.wheredidwego.domain.Region;
import com.wheredidwego.service.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = MyApplication.class)
public class RegionTest {
    @Autowired
    private RegionService regionService;

    public double x, y;

    @BeforeEach
    public void set() {
        x=127.1086228;
        y=37.4012191;

    }

    @Test
    public void checkRegion() {
        regionService.createRegion(y, x);

        Region region = regionService.findRegionById(1L);
        assertEquals("경기도", region.getProvince());
        assertEquals("성남시 분당구", region.getDistrict());

    }
}
