package org.nla.svnkit;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SvnConnector {

    private SVNRepository repository;

    public SvnConnector(SVNRepository repository) {
        this.repository = repository;
    }

    public List<String> listEntries(String path) {
        List<String> toReturn = new ArrayList<String>();
        try {
            Collection<?> entries = repository.getDir(path, -1, null, (Collection<?>) null);
            Iterator<?> iterator = entries.iterator();
            while (iterator.hasNext()) {
                SVNDirEntry entry = (SVNDirEntry) iterator.next();

                toReturn.add("/" + (path.equals("") ? "" : path + "/") + entry.getName()
                        + " ( author: '" + entry.getAuthor() + "'; revision: " + entry.getRevision()
                        + "; date: " + entry.getDate() + ")");

                if (entry.getKind() == SVNNodeKind.DIR) {
                    toReturn.addAll(listEntries((path.equals("")) ? entry.getName() : path + "/" + entry.getName()));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return toReturn;
    }
}
