package com.vmware.buildweb.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Represents information about the machine used to perform a build.
 */
public class BuildMachine {

    @SerializedName("hostname")
    public String hostName;

    @SerializedName("hosttype")
    public String hostType;

    public boolean nonLauncherMachine() {
        return hostName != null && !hostName.startsWith("build-launcher");
    }
}
