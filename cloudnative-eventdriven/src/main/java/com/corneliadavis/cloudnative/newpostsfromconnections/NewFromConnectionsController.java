package com.corneliadavis.cloudnative.newpostsfromconnections;

import com.corneliadavis.cloudnative.newpostsfromconnections.localstorage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@RefreshScope
@RestController
public class NewFromConnectionsController {

    private static final Logger logger = LoggerFactory.getLogger(NewFromConnectionsController.class);

    private MUserRepository mUserRepository;
    private MPostRepository mPostRepository;
    private MConnectionRepository mConnectionRepository;

    @Autowired
    public NewFromConnectionsController(MUserRepository mUserRepository,
                                        MPostRepository mPostRepository,
                                        MConnectionRepository mConnectionRepository) {
        this.mUserRepository = mUserRepository;
        this.mPostRepository = mPostRepository;
        this.mConnectionRepository = mConnectionRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value="/connectionsNewPosts/{username}")
    public Iterable<PostSummary> getByUsername(@PathVariable("username") String username, HttpServletResponse response) {

        //loadTestData();

        Iterable<PostSummary> postSummaries;
        logger.info("getting posts for user network " + username);

        //postSummaries = mPostRepository.findAllOfThem();
        //postSummaries = mPostRepository.findByUsername(username);
        postSummaries = mPostRepository.findForUsersConnections(username);

        return postSummaries;
    }

    private void loadTestData () {
        MUser user1 = new MUser(1L, "Cornelia", "cdavisafc");
        mUserRepository.save(user1);
        MUser user2 = new MUser(2L, "Max", "madmax");
        mUserRepository.save(user2);
        MUser user3 = new MUser(3L, "Glen", "gmaxdavis");
        mUserRepository.save(user3);

        MPost post1 = new MPost(1L, new Date(), 2L, "Max Title");
        post1.setmUser(user2);
        mPostRepository.save(post1);
        MPost post2 = new MPost(2L, new Date(), 1L, "Cornelia Title");
        post2.setmUser(user1);
        mPostRepository.save(post2);
        post2 = new MPost(3L, new Date(), 1L, "Cornelia Title2");
        post2.setmUser(user1);
        mPostRepository.save(post2);
        post2 = new MPost(4L, new Date(), 3L, "Glen Title");
        post2.setmUser(user3);
        mPostRepository.save(post2);

        MConnection connection = new MConnection(1L, 2L, 1L);
        connection.setFollowerUser(user2);
        connection.setFollowedUser(user1);
        mConnectionRepository.save(connection);
        connection = new MConnection(2L, 1L, 2L);
        connection.setFollowerUser(user1);
        connection.setFollowedUser(user2);
        mConnectionRepository.save(connection);
        connection = new MConnection(3L, 1L, 3L);
        connection.setFollowerUser(user1);
        connection.setFollowedUser(user3);
        mConnectionRepository.save(connection);

    }
}
