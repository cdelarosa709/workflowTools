package com.vmware.action.base;

import com.vmware.config.WorkflowConfig;
import com.vmware.util.StringUtils;

/**
 * Common functionality for actions that create a git commit.
 */
public abstract class BaseCommitCreateAction extends BaseCommitAction {
    public BaseCommitCreateAction(WorkflowConfig config) {
        super(config);
    }

    @Override
    public String failWorkflowIfConditionNotMet() {
        String reasonForFailing = gitRepoOrPerforceClientCannotBeUsed();
        if (StringUtils.isNotBlank(reasonForFailing)) {
            return reasonForFailing;
        }
        return super.failWorkflowIfConditionNotMet();
    }

    @Override
    public String cannotRunAction() {
        if (!draft.hasData()) {
            return "there no information set for the commit message";
        }
        return super.cannotRunAction();
    }

    @Override
    public void process() {
        String description = draft.toText(commitConfig);
        if (git.workingDirectoryIsInGitRepo()) {
            commitUsingGit(description);
        } else if (StringUtils.isNotBlank(perforceClientConfig.perforceClientName)) {
            commitUsingPerforce(description);
        }
    }

    protected void commitUsingGit(String description) {
        git.commit(draft.toText(commitConfig));
    }

    protected void commitUsingPerforce(String description) {
        log.info("Updating changelist description for {}", draft.perforceChangelistId);
        serviceLocator.getPerforce().updatePendingChangelist(draft.perforceChangelistId, description);
    }
}
