package org.nla.jauntscraper;

public class Main {

    public static void main(String[] args) throws Exception {

        AuchanDirectScraper auchanDirectScraper = new AuchanDirectScraper();
        auchanDirectScraper.scrape();
        auchanDirectScraper.persist();

        SimplyMarketScraper simplyMarketScraper = new SimplyMarketScraper();
        simplyMarketScraper.scrape();
        simplyMarketScraper.persist();
    }
}
