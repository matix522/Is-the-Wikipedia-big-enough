package sample;

import org.junit.jupiter.api.Test;

public class RandomTests {
    @Test
    void WikipediaTest(){
        WikipediaPageLoader a = new WikipediaPageLoader();

        String s = a.loadPage("/wiki/Adolf_Hitler");
        System.out.println(s);
    }
}
