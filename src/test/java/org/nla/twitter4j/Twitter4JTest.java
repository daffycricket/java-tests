package org.nla.twitter4j;

import org.junit.Test;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class Twitter4JTest {

    @Test
    public void testPostToTwitter() {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;
        try {
            status = twitter.updateStatus("This is a test tweet #TestTweet");
            assertThat(status, is(not(nullValue())));
            twitter.destroyStatus(status.getId());
        } catch (TwitterException e) {
            fail(e.getMessage());
        }
    }
}
