package test;

import impl.ContactManagerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.ContactManager;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ContactManager tests
 * Split up the tests as the main test class was getting quite large
 *
 * This class tests the Constructor and Flush methods in ContactManager
 *
 * @author lmignot
 */
public class ContactManagerTest {

    private ContactManager cMgr;

    @Before
    public void setUp() {
        cMgr = new ContactManagerImpl();
    }

    @After
    public void tearDown() {
        cMgr = null;
    }

    /* =================== CONSTRUCTOR, FLUSH, ETC... =================== */

    @Test
    public void testConstructAContactManager () {
        cMgr = new ContactManagerImpl();
        assertNotNull(cMgr);
    }

    @Test
    public void testConstructAUniqueContactManager () {
        ContactManager cm = new ContactManagerImpl();

        assertNotNull(cm);

        assertNotNull(cMgr);
        assertNotEquals(cMgr,cm);
    }

}
