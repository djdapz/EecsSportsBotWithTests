package sportsbot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportsbot.model.City;

import static org.junit.Assert.*;

/**
 * Created by devondapuzzo on 5/15/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RosterServiceTest {

    @Autowired
    private RosterService rosterService;

    @Test
    public void getCty() throws Exception {
        City chicago = rosterService.getCity("Chicago");
        City chicago1 = rosterService.getCity("chicago");

        assertEquals(chicago1, chicago);
    }

}