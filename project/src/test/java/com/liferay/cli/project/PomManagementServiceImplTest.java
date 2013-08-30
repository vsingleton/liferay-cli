package com.liferay.cli.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import com.liferay.cli.file.monitor.FileMonitorService;
import com.liferay.cli.metadata.MetadataDependencyRegistry;
import com.liferay.cli.metadata.MetadataService;
import com.liferay.cli.process.manager.FileManager;
import com.liferay.cli.shell.Shell;
import com.liferay.cli.support.osgi.OSGiUtils;
import com.liferay.cli.support.util.FileUtils;
import org.w3c.dom.Element;

import com.liferay.cli.project.PomManagementServiceImpl;
import com.liferay.cli.project.ProjectMetadata;
import com.liferay.cli.project.maven.Pom;
import com.liferay.cli.project.maven.PomFactory;

/**
 * Unit test of {@link PomManagementServiceImpl}
 * 
 * @author Andrew Swan
 * @since 1.2.0
 */
public class PomManagementServiceImplTest {

    private static final String ROOT_MODULE_NAME = "";

    @Mock private FileManager mockFileManager;
    @Mock private FileMonitorService mockFileMonitorService;
    @Mock private MetadataDependencyRegistry mockMetadataDependencyRegistry;
    @Mock private MetadataService mockMetadataService;
    @Mock private PomFactory mockPomFactory;
    @Mock private Shell mockShell;
    // Fixture
    private PomManagementServiceImpl service;

    private String getCanonicalPath(final String relativePath) {
        final String systemDependentPath = relativePath.replace("/",
                File.separator);
        final URL resource = getClass().getResource(systemDependentPath);
        assertNotNull("Can't find '" + systemDependentPath
                + "' on the classpath of " + getClass().getName(), resource);
        try {
            return new File(resource.toURI()).getCanonicalPath();
        }
        catch (final Exception e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    private Pom getMockPom(final String moduleName, final String canonicalPath) {
        final Pom mockPom = mock(Pom.class);
        when(mockPom.getModuleName()).thenReturn(moduleName);
        when(mockPom.getPath()).thenReturn(canonicalPath);
        when(
                mockPomFactory.getInstance(any(Element.class),
                        eq(canonicalPath), eq(moduleName))).thenReturn(mockPom);
        return mockPom;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new PomManagementServiceImpl();
        service.fileManager = mockFileManager;
        service.fileMonitorService = mockFileMonitorService;
        service.metadataDependencyRegistry = mockMetadataDependencyRegistry;
        service.metadataService = mockMetadataService;
        service.pomFactory = mockPomFactory;
        service.shell = mockShell;
    }

    /**
     * Sets the working directory for the service under test
     * 
     * @param relativePath the desired working directory relative to the
     *            "o.s.r.project" package
     * @throws IOException
     */
    private void setUpWorkingDirectory(final String relativePath)
            throws IOException {
        final ComponentContext mockComponentContext = mock(ComponentContext.class);
        final BundleContext mockBundleContext = mock(BundleContext.class);
        when(mockComponentContext.getBundleContext()).thenReturn(
                mockBundleContext);
        final File workingDirectory = new File(
                "target/test-classes/org/springframework/roo/project",
                relativePath);
        when(
                mockBundleContext
                        .getProperty(OSGiUtils.ROO_WORKING_DIRECTORY_PROPERTY))
                .thenReturn(workingDirectory.getCanonicalPath());
        service.activate(mockComponentContext);
    }

    @Test
    public void testGetPomOfSingleModuleProjectWhenParentHasNoRelativePath()
            throws Exception {
        // Set up
        setUpWorkingDirectory("single");
        final String canonicalPath = getCanonicalPath("single/pom.xml");
        when(
                mockFileMonitorService
                        .getDirtyFiles(PomManagementServiceImpl.class.getName()))
                .thenReturn(Arrays.asList(canonicalPath));
        final Pom mockPom = getMockPom(ROOT_MODULE_NAME, canonicalPath);

        // Invoke
        final Collection<Pom> poms = service.getPoms();

        // Check
        assertEquals(1, poms.size());
        assertEquals(mockPom, poms.iterator().next());
        verifyProjectMetadataNotification(ROOT_MODULE_NAME);
    }

    @Test
    public void testGetPomsOfMultiModuleProjectWhenChildIsDirty()
            throws Exception {
        // Set up
        setUpWorkingDirectory("multi");
        final String rootPom = FileUtils.getSystemDependentPath("multi",
                "pom.xml");
        final String rootPomCanonicalPath = getCanonicalPath(rootPom);
        final String childPom = FileUtils.getSystemDependentPath("multi",
                "foo-child", "pom.xml");
        final String childPomCanonicalPath = getCanonicalPath(childPom);
        final Collection<String> dirtyFiles = Arrays
                .asList(childPomCanonicalPath);
        when(
                mockFileMonitorService
                        .getDirtyFiles(PomManagementServiceImpl.class.getName()))
                .thenReturn(dirtyFiles);

        final Pom mockRootPom = getMockPom(ROOT_MODULE_NAME,
                rootPomCanonicalPath);
        final String childModuleName = "foo-child";
        final Pom mockChildPom = getMockPom(childModuleName,
                childPomCanonicalPath);

        when(mockFileManager.getInputStream(rootPomCanonicalPath)).thenReturn(
                getClass().getResourceAsStream(rootPom));
        when(mockFileManager.getInputStream(childPomCanonicalPath)).thenReturn(
                getClass().getResourceAsStream(childPom));

        service.addPom(mockRootPom);

        // Invoke
        final Collection<Pom> poms = service.getPoms();

        // Check
        final Collection<Pom> expectedPoms = Arrays.asList(mockRootPom,
                mockChildPom);
        assertEquals(expectedPoms.size(), poms.size());
        assertTrue(poms.containsAll(expectedPoms));
        verifyProjectMetadataNotification(childModuleName);
    }

    @Test
    public void testGetPomsOfMultiModuleProjectWhenParentAndChildAreDirty()
            throws Exception {
        // Set up
        setUpWorkingDirectory("multi");
        final String rootPom = "multi/pom.xml";
        final String rootPomCanonicalPath = getCanonicalPath(rootPom);
        final String childPom = "multi/foo-child/pom.xml";
        final String childPomCanonicalPath = getCanonicalPath(childPom);
        final Collection<String> dirtyFiles = Arrays.asList(
                rootPomCanonicalPath, childPomCanonicalPath);
        when(
                mockFileMonitorService
                        .getDirtyFiles(PomManagementServiceImpl.class.getName()))
                .thenReturn(dirtyFiles);

        final Pom mockRootPom = getMockPom(ROOT_MODULE_NAME,
                rootPomCanonicalPath);
        final String childModuleName = "foo-child";
        final Pom mockChildPom = getMockPom(childModuleName,
                childPomCanonicalPath);

        when(mockFileManager.getInputStream(rootPomCanonicalPath)).thenReturn(
                getClass().getResourceAsStream(rootPom));
        when(mockFileManager.getInputStream(childPomCanonicalPath)).thenReturn(
                getClass().getResourceAsStream(childPom));

        // Invoke
        final Collection<Pom> poms = service.getPoms();

        // Check
        final Collection<Pom> expectedPoms = Arrays.asList(mockRootPom,
                mockChildPom);
        assertEquals(expectedPoms.size(), poms.size());
        assertTrue(poms.containsAll(expectedPoms));
        verifyProjectMetadataNotification(ROOT_MODULE_NAME, childModuleName);
    }

    @Test
    public void testGetPomsWhenNoPomsAreDirty() {
        // Set up
        final Collection<String> dirtyFiles = Arrays.asList("not-a-pom.txt");
        when(
                mockFileMonitorService
                        .getDirtyFiles(PomManagementServiceImpl.class.getName()))
                .thenReturn(dirtyFiles);

        // Invoke
        final Collection<Pom> poms = service.getPoms();

        // Check
        assertEquals(0, poms.size());
    }

    @Test
    public void testGetPomsWhenOneEmptyPomIsDirty() throws Exception {
        // Set up
        final Collection<String> dirtyFiles = Arrays
                .asList(getCanonicalPath("empty/pom.xml"));
        when(
                mockFileMonitorService
                        .getDirtyFiles(PomManagementServiceImpl.class.getName()))
                .thenReturn(dirtyFiles);

        // Invoke
        final Collection<Pom> poms = service.getPoms();

        // Check
        assertEquals(0, poms.size());
    }

    @Test
    public void testGetPomsWhenOneNonExistantPomIsDirty() {
        // Set up
        final Collection<String> dirtyFiles = Arrays
                .asList("/users/jbloggs/clinic/pom.xml");
        when(
                mockFileMonitorService
                        .getDirtyFiles(PomManagementServiceImpl.class.getName()))
                .thenReturn(dirtyFiles);

        // Invoke
        final Collection<Pom> poms = service.getPoms();

        // Check
        assertEquals(0, poms.size());
    }

    private void verifyProjectMetadataNotification(final String... moduleNames) {
        for (final String moduleName : moduleNames) {
            final String projectMetadataId = ProjectMetadata
                    .getProjectIdentifier(moduleName);
            verify(mockMetadataService).evictAndGet(projectMetadataId);
            verify(mockMetadataDependencyRegistry).notifyDownstream(
                    projectMetadataId);
        }
    }
}
