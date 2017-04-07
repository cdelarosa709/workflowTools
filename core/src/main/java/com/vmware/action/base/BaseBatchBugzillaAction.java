package com.vmware.action.base;

import com.vmware.bugzilla.Bugzilla;
import com.vmware.bugzilla.domain.Bug;
import com.vmware.config.WorkflowConfig;
import com.vmware.jira.domain.Issue;
import com.vmware.jira.domain.IssueTypeDefinition;

public abstract class BaseBatchBugzillaAction extends BaseIssuesProcessingAction {

    protected Bugzilla bugzilla;

    public BaseBatchBugzillaAction(WorkflowConfig config) {
        super(config);
    }

    @Override
    public void asyncSetup() {
        this.bugzilla = serviceLocator.getBugzilla();
    }

    @Override
    public void preprocess() {
        this.bugzilla.setupAuthenticatedConnection();
    }

    @Override
    public String cannotRunAction() {
        if (config.disableBugzilla) {
            return "Bugzilla is disabled by config property disableBugzilla";
        }
        return super.cannotRunAction();
    }

    protected Issue createIssueFromBug(Bug bug) {
        String summary = "[BZ-" + bug.getKey() + "] " + bug.getSummary();
        String description = bug.getWebUrl() + "\n" + bug.getDescription();
        Issue matchingIssue = new Issue(IssueTypeDefinition.Bug, config.defaultJiraProject,
                config.defaultJiraComponent, summary, description, null);
        return matchingIssue;
    }
}
