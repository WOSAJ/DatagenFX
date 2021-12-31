package tk.wosaj.datagenfx.version;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tk.wosaj.datagenfx.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

public class UpdateManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static UpdateBundle checkUpdates(Properties props) throws URISyntaxException, IOException {
        UpdateData serverData = getUpdateData();
        return new UpdateBundle(getStatus(serverData, props), serverData);
    }

    public static UpdateData getUpdateData() throws URISyntaxException, IOException {
        URL url = new URI(Application.properties.getProperty("update.jsonurl")).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        StringBuilder out;
        try (var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String temp;
            out = new StringBuilder();
            while ((temp = reader.readLine()) != null) out.append(temp);
        }
        return gson.fromJson(out.toString(), UpdateData.class);
    }

    public static UpdateStatus getStatus(UpdateData compatibleData, Properties props) throws IOException {
        var rawData = props.getProperty("version").split(" ");
        if(rawData.length != 2) throw new IOException();
        if(compatibleData.getStatus().equals("RELEASE") && rawData[0].equals("RELEASE")) {
            return switch(versionComparator.compare(rawData[1], compatibleData.getVersion())) {
                case 0 -> UpdateStatus.NEWEST;
                case 1 -> UpdateStatus.OUTDATED;
                case -1 -> UpdateStatus.NEW_VERSION_AVAILABLE;
                default -> UpdateStatus.UNKNOWN;
            };
        } else if(rawData[0].equals("PRERELEASE") && (
                    compatibleData.getStatus().equals("PRERELEASE") ||
                    compatibleData.getStatus().equals("RELEASE"))) {
            if(compatibleData.getStatus().equals("RELEASE")) return UpdateStatus.NEW_VERSION_AVAILABLE;
            return switch(versionComparator.compare(rawData[1], compatibleData.getVersion())) {
                case 0 -> UpdateStatus.NEWEST;
                case 1 -> UpdateStatus.OUTDATED;
                case -1 -> UpdateStatus.NEW_VERSION_AVAILABLE;
                default -> UpdateStatus.UNKNOWN;
            };
        } else if(rawData[0].equals("BETA") && (
                compatibleData.getStatus().equals("PRERELEASE") ||
                        compatibleData.getStatus().equals("RELEASE") ||
                        compatibleData.getStatus().equals("BETA"))) {
            if(compatibleData.getStatus().equals("PRERELEASE")) return UpdateStatus.NEW_VERSION_AVAILABLE;
            if(compatibleData.getStatus().equals("RELEASE")) return UpdateStatus.NEW_VERSION_AVAILABLE;
            return switch (versionComparator.compare(rawData[1], compatibleData.getVersion())) {
                case 0 -> UpdateStatus.NEWEST;
                case 1 -> UpdateStatus.OUTDATED;
                case -1 -> UpdateStatus.NEW_VERSION_AVAILABLE;
                default -> UpdateStatus.UNKNOWN;
            };
        } else if(rawData[0].equals("ALPHA") && (
                compatibleData.getStatus().equals("PRERELEASE") ||
                        compatibleData.getStatus().equals("RELEASE")) ||
                        compatibleData.getStatus().equals("BETA") ||
                        compatibleData.getStatus().equals("ALPHA")) {
            if(!compatibleData.getStatus().equals("ALPHA")) return UpdateStatus.NEW_VERSION_AVAILABLE;
            return switch (versionComparator.compare(rawData[1], compatibleData.getVersion())) {
                case 0 -> UpdateStatus.NEWEST;
                case 1 -> UpdateStatus.OUTDATED;
                case -1 -> UpdateStatus.NEW_VERSION_AVAILABLE;
                default -> UpdateStatus.UNKNOWN;
            };
        }
        return UpdateStatus.UNKNOWN;
    }

    public static final Comparator<String> versionComparator = (o1, o2) -> {
        var split1 = Arrays.stream(o1.split("\\.")).map(Integer::parseInt).toList();
        var split2 = Arrays.stream(o2.split("\\.")).map(Integer::parseInt).toList();

        if(split1.equals(split2)) return 0;

        if(split1.get(0) > split2.get(0) ||
           split1.get(1) > split2.get(1) ||
           split1.get(2) > split2.get(2)) return 1;

        if(split1.get(0) < split2.get(0) ||
           split1.get(1) < split2.get(1) ||
           split1.get(2) < split2.get(2)) return -1;
        return -2;
    };

    public enum UpdateStatus {
        UNKNOWN,
        OUTDATED,
        NEWEST,
        NEW_VERSION_AVAILABLE
    }
    public record UpdateBundle(UpdateStatus status, UpdateData data) {}
}