import java.util.Collections;
import static com.vmware.scm.FileChangeType.copied;
    private String perforceChangelistId;
    public String getPerforceChangelistId() {
        return perforceChangelistId;
    }

    public void setPerforceChangelistId(String perforceChangelistId) {
        this.perforceChangelistId = perforceChangelistId;
    }

    public boolean matchesOneOf(FileChangeType... changeTypes) {
        for (FileChangeType changeType : changeTypes) {
            if (this.changeType == changeType) {
                return true;
            }
        }
        return false;
    }

            case "change":
                setPerforceChangelistId(value);
                break;
    public String  diffGitLine() {
                    throw new RuntimeException("Expected to find file mode for new file " + bFile);
                }
        List<String> filesFromChangeToCheck = filesAffected;
        List<String> filesFromOtherChangeToCheck = that.filesAffected;
            } else if (that.changeType == copied) {
                changeTypeToUseForComparision = added;
                filesFromOtherChangeToCheck = Collections.singletonList(that.getLastFileAffected());
            } else if (changeType == copied && that.changeType == added) {
                changeTypeToUseForComparision = copied;
                filesFromChangeToCheck = Collections.singletonList(getLastFileAffected());
        return changeType == changeTypeToUseForComparision && Objects.deepEquals(filesFromChangeToCheck, filesFromOtherChangeToCheck);