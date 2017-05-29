package sportsbot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sportsbot.enums.Sport;
import sportsbot.model.position.Position;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by devondapuzzo on 5/28/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PositionsServiceTest {

    @Autowired
    private PositionsService positionsService;

    @Test
    public void basicFindPositionsBaseball() throws Exception{
        Position pitcher = positionsService.findPosition(Sport.BASEBALL, "pitcher");
        Position firstBase = positionsService.findPosition(Sport.BASEBALL, "first base");

        Position firstBase1 = positionsService.findPosition(Sport.BASEBALL, "1st base");

        Position firstBase2 = positionsService.findPosition(Sport.BASEBALL, "first");

        Position firstBase3 = positionsService.findPosition(Sport.BASEBALL, "1st");

        assertEquals(firstBase, firstBase1);
        assertEquals(firstBase, firstBase2);
        assertEquals(firstBase, firstBase3);
        assertTrue(pitcher.getAbbreviation().equals("P"));
    }

}