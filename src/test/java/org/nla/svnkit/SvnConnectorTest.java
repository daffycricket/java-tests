package org.nla.svnkit;

import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.util.List;

import static org.junit.Assert.fail;

public class SvnConnectorTest {

    private static final String SVN_URL = "http://tarotdroid.googlecode.com/svn/trunk/TarotDroid.Biz.Tests";

    private SVNRepository svnRepository;

    private SvnConnector svnConnector;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        svnRepository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(SVN_URL));
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("xxx", "yyy");
        svnRepository.setAuthenticationManager(authManager);
        svnConnector = new SvnConnector(svnRepository);
    }

    @SuppressWarnings("unused")
    @Test
    public void test() throws Exception {

        SVNNodeKind nodeKind = svnRepository.checkPath("", -1);
        if (nodeKind == SVNNodeKind.NONE) {
            fail("There is no entry at '" + SVN_URL + "'.");
        } else if (nodeKind == SVNNodeKind.FILE) {
            fail("The entry at '" + SVN_URL + "' is a file while a directory was expected.");
        }

        List<String> entries = svnConnector.listEntries("");
        fail("Not yet implemented");
    }
}
