package se.bjurr.sscc;

import static java.lang.Boolean.TRUE;
import static se.bjurr.sscc.SSCCTestConstants.COMMIT_MESSAGE_JIRA;
import static se.bjurr.sscc.data.SSCCChangeSetBuilder.changeSetBuilder;
import static se.bjurr.sscc.settings.SSCCSettings.SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL;
import static se.bjurr.sscc.settings.SSCCSettings.SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE;
import static se.bjurr.sscc.settings.SSCCSettings.SETTING_REQUIRE_MATCHING_COMMITTER_EMAIL;
import static se.bjurr.sscc.util.RefChangeBuilder.refChangeBuilder;

import java.io.IOException;

import org.junit.Test;

import se.bjurr.sscc.data.SSCCPerson;

public class MatchingEmailTest {
 @Test
 public void testCommitMustMatchEmailInStashAndOnlyOneOfTwoCommitsIsOk() throws IOException {
  refChangeBuilder()
    .withChangeSet(
      changeSetBuilder().withId("1").withCommitter(new SSCCPerson("Tomas Bjerre", "tomas.bjerre@one.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withChangeSet(
      changeSetBuilder().withId("2").withCommitter(new SSCCPerson("Tompa B", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withStashEmail("tomas.bjerre@two.site")
    .withStashDisplayName("Tompa B")
    .withSetting(SETTING_REQUIRE_MATCHING_COMMITTER_EMAIL, TRUE)
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE, "Email in Stash not same as in commit")
    .build()
    .run()
    .hasTrimmedFlatOutput(
      "refs/heads/master e2bc4ed003 -> af35d5c1a4   1 Tomas <my@email.com> >>> SB-5678 fixing stuff  - Stash: 'tomas.bjerre@two.site' != Commit: 'tomas.bjerre@one.site'   Email in Stash not same as in commit")
    .wasRejected();
 }

 @Test
 public void testCommitMustMatchEmailInStashAndOnlyOneOfTwoCommitsIsOkReverseOrder() throws IOException {
  refChangeBuilder()
    .withChangeSet(
      changeSetBuilder().withId("1").withCommitter(new SSCCPerson("Tompa B", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withChangeSet(
      changeSetBuilder().withId("2").withCommitter(new SSCCPerson("Tomas Bjerre", "tomas.bjerre@one.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withStashEmail("tomas.bjerre@two.site")
    .withStashDisplayName("Tompa B")
    .withSetting(SETTING_REQUIRE_MATCHING_COMMITTER_EMAIL, TRUE)
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE, "Email in Stash not same as in commit")
    .build()
    .run()
    .hasTrimmedFlatOutput(
      "refs/heads/master e2bc4ed003 -> af35d5c1a4   2 Tomas <my@email.com> >>> SB-5678 fixing stuff  - Stash: 'tomas.bjerre@two.site' != Commit: 'tomas.bjerre@one.site'   Email in Stash not same as in commit")
    .wasRejected();
 }

 @Test
 public void testCommitIsAcceptedWhenEmailMustMatchEmailInStashAndTwoOfTwoCommitsHaveDifferentNames()
   throws IOException {
  refChangeBuilder()
    .withChangeSet(
      changeSetBuilder().withId("1").withCommitter(new SSCCPerson("Tomas Bjerre", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withChangeSet(
      changeSetBuilder().withId("2").withCommitter(new SSCCPerson("Tompa B", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build()).withStashEmail("tomas.bjerre@two.site")
    .withStashDisplayName("Tompa B").withSetting(SETTING_REQUIRE_MATCHING_COMMITTER_EMAIL, TRUE)
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE, "Email in Stash not same as in commit").build().run()
    .hasNoOutput().wasAccepted();
 }

 @Test
 public void testCommitMustMatchAuthorEmailInStashAndOnlyOneOfTwoCommitsIsOk() throws IOException {
  refChangeBuilder()
    .withChangeSet(
      changeSetBuilder().withId("1").withAuthor(new SSCCPerson("Tomas Bjerre", "tomas.bjerre@one.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withChangeSet(
      changeSetBuilder().withId("2").withAuthor(new SSCCPerson("Tompa B", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withStashEmail("tomas.bjerre@two.site")
    .withStashDisplayName("Tompa B")
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL, TRUE)
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE, "Email in Stash not same as in commit")
    .build()
    .run()
    .wasRejected()
    .hasTrimmedFlatOutput(
      "refs/heads/master e2bc4ed003 -> af35d5c1a4   1 Tomas Bjerre <tomas.bjerre@one.site> >>> SB-5678 fixing stuff  - Stash: 'tomas.bjerre@two.site' != Commit: 'tomas.bjerre@one.site'   Email in Stash not same as in commit");
 }

 @Test
 public void testCommitMustMatchAuthorEmailInStashAndOnlyOneOfTwoCommitsIsOkReverseOrder() throws IOException {
  refChangeBuilder()
    .withChangeSet(
      changeSetBuilder().withId("1").withAuthor(new SSCCPerson("Tompa B", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withChangeSet(
      changeSetBuilder().withId("2").withAuthor(new SSCCPerson("Tomas Bjerre", "tomas.bjerre@one.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withStashEmail("tomas.bjerre@two.site")
    .withStashDisplayName("Tompa B")
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL, TRUE)
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE, "Email in Stash not same as in commit")
    .build()
    .run()
    .wasRejected()
    .hasTrimmedFlatOutput(
      "refs/heads/master e2bc4ed003 -> af35d5c1a4   2 Tomas Bjerre <tomas.bjerre@one.site> >>> SB-5678 fixing stuff  - Stash: 'tomas.bjerre@two.site' != Commit: 'tomas.bjerre@one.site'   Email in Stash not same as in commit");
 }

 @Test
 public void testCommitIsAcceptedWhenAuthorEmailMustMatchEmailInStashAndTwoOfTwoCommitsHaveDifferentNames()
   throws IOException {
  refChangeBuilder()
    .withChangeSet(
      changeSetBuilder().withId("1").withAuthor(new SSCCPerson("Tomas Bjerre", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build())
    .withChangeSet(
      changeSetBuilder().withId("2").withAuthor(new SSCCPerson("Tompa B", "tomas.bjerre@two.site"))
        .withMessage(COMMIT_MESSAGE_JIRA).build()).withStashEmail("tomas.bjerre@two.site")
    .withStashDisplayName("Tompa B").withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL, TRUE)
    .withSetting(SETTING_REQUIRE_MATCHING_AUTHOR_EMAIL_MESSAGE, "Email in Stash not same as in commit").build().run()
    .hasNoOutput().wasAccepted();
 }
}
