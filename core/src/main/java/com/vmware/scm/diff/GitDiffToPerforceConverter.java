import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
    private Logger log = LoggerFactory.getLogger(this.getClass());

            Matcher infoMatcher = parseLine(depotFileChecked, depotFileInfo, depotFileInfoPattern);
            if (infoMatcher == null) {
                whereFilesToCheck.add(depotFileChecked);
                continue;
            }
    private Matcher parseLine(String fileName, String line, Pattern patternToUse) {
            log.warn("Expected {} to exist in perforce: unexpected response {} for p4 files command, using p4 where to get file path", fileName, line);
            return null;