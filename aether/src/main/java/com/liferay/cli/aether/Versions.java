package com.liferay.cli.aether;

import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

public class Versions
{

    public static String getAvailable( String group, String artifact ) throws Exception {
    	
    	String available = "";

    	RepositorySystem repoSystem = Repositories.newRepositorySystem();
    	if (repoSystem == null) {
			System.err.println("main: repoSystem == null");
		} else {

			RepositorySystemSession session = Repositories.newSession(repoSystem);

			Artifact defaultArtifact = new DefaultArtifact(group + ":" + artifact + ":" + "[0,)");

			// RemoteRepository repo = Repositories.newCentralRepository();
			RemoteRepository central = new RemoteRepository.Builder("central", "default",
					"http://repo1.maven.org/maven2/").build();

			VersionRangeRequest rangeRequest = new VersionRangeRequest();
			rangeRequest.setArtifact(defaultArtifact);
			rangeRequest.addRepository(central);

			VersionRangeResult rangeResult = repoSystem.resolveVersionRange(session, rangeRequest);

			List<Version> versions = rangeResult.getVersions();
			if (versions == null) {
				System.out.println("getAvailable: newestVersion == null");
			} else {
				System.out.println("getAvailable: versions " + versions);
				available = versions.toString();
			}
		}
    	return available;
    }
    
    public static String getLatestMinor( String group, String artifact, String major1and2) throws Exception {
    	
    	String version = "";

    	RepositorySystem repoSystem = Repositories.newRepositorySystem();
    	if (repoSystem == null) {
			System.err.println("main: repoSystem == null");
		} else {

			RepositorySystemSession session = Repositories.newSession(repoSystem);

			Artifact defaultArtifact = new DefaultArtifact(group + ":" + artifact + ":" + "[0,)");

			RemoteRepository central = new RemoteRepository.Builder("central", "default",
					"http://repo1.maven.org/maven2/").build();

			VersionRangeRequest rangeRequest = new VersionRangeRequest();
			rangeRequest.setArtifact(defaultArtifact);
			rangeRequest.addRepository(central);

			VersionRangeResult rangeResult = repoSystem.resolveVersionRange(session, rangeRequest);

			Version newestVersion = rangeResult.getHighestVersion();
			
			if (newestVersion == null) {
				System.out.println("getLatestMinor: newestVersion == null");
			} else {

				System.out.println("getLatestMinor: Newest version " + newestVersion + " from repository "
						+ rangeResult.getRepository(newestVersion));

				version = newestVersion.toString();
			}
		}
    	
    	return version;
    }

}