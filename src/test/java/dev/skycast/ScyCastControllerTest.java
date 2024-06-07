package dev.skycast;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ScyCastControllerTest {

    private ScyCastController controller;

    @BeforeEach
    void setUp() {
        controller = new ScyCastController();
    }

    @Test
    void testGetDirectionName() {
        assertEquals("Север", controller.getDirectionName(0));
        assertEquals("Северо-восток", controller.getDirectionName(45));
        assertEquals("Восток", controller.getDirectionName(90));
        assertEquals("Юго-восток", controller.getDirectionName(135));
        assertEquals("Юг", controller.getDirectionName(180));
        assertEquals("Юго-запад", controller.getDirectionName(225));
        assertEquals("Запад", controller.getDirectionName(270));
        assertEquals("Северо-запад", controller.getDirectionName(315));
    }



    @Test
    void testGetLatitudeLongitude() {
        double[] latLon = controller.getLatitudeLongitude("8.8.8.8");
        assertNotNull(latLon);
        assertEquals(2, latLon.length);
    }


    @Test
    void testGetCityAndCountry() {
        String cityAndCountry = controller.getCityAndCountry("8.8.8.8");
        assertNotNull(cityAndCountry);
    }

}
