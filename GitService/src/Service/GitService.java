package Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.TypedResource;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitService {

        public GitHubClient client;
        public RepositoryService service;
        public CommitService cservice;
        public DataService dservice;
        public Repository repo;
        String repoName;
        String repoOwner;

        public static void main(String args[]){
            try {
                String repositoryName = args[0].toString();
                String repositoryOwner= args[1].toString();
                String authUser= args[3].toString();
                String authPass= args[4].toString();
                GitService gservice = new GitService(repositoryName,repositoryOwner,authUser,authPass);
                gservice.CommitUpdate(gservice, "neo_recent/recent", "[\"Recent\"]", "Recent");
                gservice.CommitUpdate(gservice, "neo_news/latestnews", "[\"NEWS\"]", "News");
                System.out.println("Done");
            } catch (IOException e) {
                System.out.println("IOException");
            e.printStackTrace();
        }
    }

        public GitService(String repositoryName, String repositoryOwner, String user, String password) throws IOException {
            client = new GitHubClient();
            client.setCredentials(user, password);
            this.repoName =repositoryName;
            this.repoOwner = repositoryOwner;
            initServices(client);
        }

        public void initServices(GitHubClient client) throws IOException{
            service = new RepositoryService(client);
            cservice = new CommitService(client);
            dservice = new DataService(client);
            repo = service.getRepository(repoOwner, repoName);
        }
        
        public User setupUser(String email, String name, String login, String avatarURL, String gravatarID, String url, int id){
            User usr = new User();
            usr.setAvatarUrl(avatarURL);
            usr.setEmail(email);
            usr.setGravatarId(gravatarID);
            usr.setName(name);
            usr.setLogin(login);
            usr.setUrl(url);
            usr.setId(id);
            return usr;    
        }
        
        public CommitUser createCommitUser(String email, String name){
            CommitUser  cUser = new CommitUser();
            cUser.setEmail(email);
            cUser.setName(name);
            return cUser;
        }

        public void CommitUpdate(GitService gservice, String filePathToUpdate, String content, String CommitTitle) throws IOException{
            String baseCommitSha = gservice.service.getBranches(gservice.repo).get(0).getCommit().getSha();
            RepositoryCommit baseCommit = gservice.cservice.getCommit(gservice.repo, baseCommitSha);
            String treeSha = baseCommit.getSha();
            
            Blob blobtest = new Blob();
            blobtest.setContent(content).setEncoding(Blob.ENCODING_UTF8);;
            String blobSha = gservice.dservice.createBlob(gservice.repo, blobtest);
            Tree baseTree = gservice.dservice.getTree(gservice.repo, treeSha);

            ArrayList<TreeEntry> trees = new ArrayList();
            TreeEntry entry = new TreeEntry();
            entry.setType(entry.TYPE_BLOB);
            entry.setSha(blobSha);
            entry.setMode(entry.MODE_BLOB);
            entry.setPath(filePathToUpdate);
            entry.setSize(blobtest.getContent().length());
            trees.add(entry);
            Tree treetest = gservice.dservice.createTree(gservice.repo, trees, baseTree.getSha());
            
            Commit commiteTest = new Commit();
            commiteTest.setMessage(CommitTitle);
            commiteTest.setTree(treetest);
            List<Commit> listOfCommits = new ArrayList<Commit>();
            listOfCommits.add(new Commit().setSha(baseCommitSha));
            commiteTest.setParents(listOfCommits);
            Commit brandnew = gservice.dservice.createCommit(gservice.repo, commiteTest);
            
            Reference reference  = gservice.dservice.getReference(gservice.repo, "heads/master");
            TypedResource ctResource = new TypedResource();
            ctResource.setSha(brandnew.getSha());
            ctResource.setType(TypedResource.TYPE_COMMIT);
            ctResource.setUrl(brandnew.getUrl());

            reference.setObject(ctResource);
            gservice.dservice.editReference(gservice.repo, reference, true);
            System.out.println("GitService Done");
        }
        
        public void CommitMultipleUpdates(GitService gservice, ArrayList filePathsToUpdate, ArrayList content, String CommitTitle) throws IOException{
            String baseCommitSha = gservice.service.getBranches(gservice.repo).get(0).getCommit().getSha();
            RepositoryCommit baseCommit = gservice.cservice.getCommit(gservice.repo, baseCommitSha);
            String treeSha = baseCommit.getSha();
     
            Tree baseTree = gservice.dservice.getTree(gservice.repo, treeSha);
            ArrayList<TreeEntry> trees = new ArrayList();
            for (int i = 0; i < filePathsToUpdate.size(); i++){
                Blob blobtest = new Blob();
                blobtest.setContent(content.get(i).toString()).setEncoding(Blob.ENCODING_UTF8);;
                String blobSha = gservice.dservice.createBlob(gservice.repo, blobtest);

                TreeEntry entry = new TreeEntry();
                entry.setType(entry.TYPE_BLOB);
                entry.setSha(blobSha);
                entry.setMode(entry.MODE_BLOB);
                entry.setPath(filePathsToUpdate.get(i).toString());
                entry.setSize(blobtest.getContent().length());
                trees.add(entry);
            }
            Tree treetest = gservice.dservice.createTree(gservice.repo, trees, baseTree.getSha());
            
            Commit commiteTest = new Commit();
            commiteTest.setMessage(CommitTitle);
            commiteTest.setTree(treetest);
            List<Commit> listOfCommits = new ArrayList<Commit>();
            listOfCommits.add(new Commit().setSha(baseCommitSha));
            commiteTest.setParents(listOfCommits);
            Commit brandnew = gservice.dservice.createCommit(gservice.repo, commiteTest);
            
            Reference reference  = gservice.dservice.getReference(gservice.repo, "heads/master");
            TypedResource ctResource = new TypedResource();
            ctResource.setSha(brandnew.getSha());
            ctResource.setType(TypedResource.TYPE_COMMIT);
            ctResource.setUrl(brandnew.getUrl());

            reference.setObject(ctResource);
            gservice.dservice.editReference(gservice.repo, reference, true);
            System.out.println("GitService Done");
        }
}
