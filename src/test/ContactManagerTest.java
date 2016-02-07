package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spec.ContactManager;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ContactManager tests
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

    @Test
    public void constructAContactManager () {
        ContactManager cm = new ContacManagerImpl();

        assertThat(cm).isNotNull();
    }
}
