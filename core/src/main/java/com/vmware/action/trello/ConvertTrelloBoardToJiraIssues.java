package com.vmware.action.trello;

import com.vmware.ServiceLocator;
import com.vmware.action.base.AbstractBatchIssuesAction;
import com.vmware.config.ActionDescription;
import com.vmware.config.WorkflowConfig;
import com.vmware.jira.domain.Issue;
import com.vmware.trello.Trello;
import com.vmware.trello.domain.Board;
import com.vmware.trello.domain.Card;
import com.vmware.trello.domain.Swimlane;
import com.vmware.utils.InputUtils;
import com.vmware.utils.Padder;
import com.vmware.utils.UrlUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@ActionDescription("Converts a Trello board into a list of jira issues. Only story point values are set in created issues.")
public class ConvertTrelloBoardToJiraIssues extends AbstractBatchIssuesAction {
    private Trello trello;

    public ConvertTrelloBoardToJiraIssues(WorkflowConfig config) throws IOException, URISyntaxException, IllegalAccessException {
        super(config);
    }

    @Override
    public void preprocess() throws IOException, URISyntaxException, IllegalAccessException {
        trello = ServiceLocator.getTrello(config.trelloUrl);
    }

    @Override
    public void process() throws IOException, IllegalAccessException, URISyntaxException, ParseException {
        Board[] openBoards = trello.getOpenBoardsForUser();

        if (openBoards.length == 0) {
            log.info("You have no open Trello boards");
            return;
        }

        projectIssues.reset();

        log.info("Enter trello board to convert to jira issues");
        Integer selection = InputUtils.readSelection(openBoards, "Trello Boards");

        Board selectedBoard = openBoards[selection];
        Swimlane[] swimlanes = trello.getSwimlanesForBoard(selectedBoard);

        Padder padder = new Padder("{} Swimlanes", selectedBoard.name);
        padder.infoTitle();
        for (Swimlane swimlane : swimlanes) {
            Integer storyPointValue = swimlane.getStoryPointValue();
            if (storyPointValue == null) {
                log.info("Skipping issues in swimlane {}", swimlane.name);
                continue;
            }

            Card[] cardsToUpdate = trello.getCardsForSwimlane(swimlane);
            if (cardsToUpdate.length == 0) {
                log.info("No cards in swimlane {}", swimlane.name);
                continue;
            }

            log.info("{} cards to process for swimlane {}", cardsToUpdate.length, swimlane.name);

            for (Card cardToUpdate : cardsToUpdate) {
                Issue issueToUpdate = new Issue(cardToUpdate.getIssueKey());
                issueToUpdate.fields.storyPoints = storyPointValue;
                issueToUpdate.fields.summary = cardToUpdate.name;
                String urlForIssue =
                        UrlUtils.addTrailingSlash(config.jiraUrl) + "browse/" + issueToUpdate.key;
                issueToUpdate.fields.description = cardToUpdate.getDescriptionWithoutJiraUrl(urlForIssue);
                issueToUpdate.fields.acceptanceCriteria = cardToUpdate.getAcceptanceCriteria();
                projectIssues.add(issueToUpdate);
            }
        }
        padder.infoTitle();
    }

}
