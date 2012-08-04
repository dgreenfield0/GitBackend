import java.io.IOException;

import util.HttpUtil;

import domain.User;

import Service.GitService;

public class MainTest {

    public static void main(String args[]){
        try {
            //Setup GitService
            String repositoryName = args[0].toString();
            String repositoryOwner= args[1].toString();
            String authUser= args[2].toString();
            String authPass= args[3].toString();
            String DataBase_Dir = "Users";
            String DataFile = "user1";
            String branch = "master";
            GitService gservice = new GitService(repositoryName,repositoryOwner,authUser,authPass, branch);
            
            //Setup Test Data
            User usr = new User();
            usr.setName("Kaygo2");
            usr.setAge("418");
            usr.setEmail("kaygo@email.com");

            //Write to Git (Commit to Database)
            gservice.CommitUpdate(gservice, DataBase_Dir+"/"+DataFile, usr.retrieveJson(), "NEWUSER");

            //Read from Git (Get from Database)
            String userData = gservice.retreiveData(DataBase_Dir, DataFile);
            System.out.println("User data from git service:"+ userData);

        } catch (IOException e) {
            System.out.println("IOException");
        e.printStackTrace();
    }
}
    
}
