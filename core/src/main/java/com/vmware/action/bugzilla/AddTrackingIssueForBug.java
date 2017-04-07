package com.vmware.action.bugzilla;

import com.vmware.action.base.BaseBatchBugzillaAction;
import com.vmware.bugzilla.domain.Bug;
import com.vmware.config.ActionDescription;
import com.vmware.config.WorkflowConfig;
import com.vmware.jira.domain.Issue;
import com.vmware.util.input.InputUtils;

@ActionDescription("Adds a Jira Issue to use for tracking a bugzill bug.")
public class AddTrackingIssueForBug extends BaseBatchBugzillaAction {

    public AddTrackingIssueForBug(WorkflowConfig config) {
        super(config);
    }

    @Override
    public void process() {
        int bugNumber = InputUtils.readValueUntilValidInt("Enter bugzilla bug number");

        Bug bug = bugzilla.getBugById(bugNumber);
        String existingTrackingIssueKey = bug.getTrackingIssueKey();
        if (existingTrackingIssueKey != null) {
            log.info("Bug {} is already being tracked by issue {}", bugNumber, existingTrackingIssueKey);
            String createNewIssue = InputUtils.readValueUntilNotBlank("Create new tracking issue? [Y/N]");
            if (!"Y".equalsIgnoreCase(createNewIssue)) {
                return;
            }
        }
        Issue trackingIssue = createIssueFromBug(bug);
        projectIssues.add(trackingIssue);

        log.info("A Jira Issue will be created in Jira Project {} to track bug {}: {}", config.defaultJiraProject,
                trackingIssue.matchingBugzillaNumber(config.bugzillaUrl), bug.getSummary());
    }
}
