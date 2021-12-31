package tk.wosaj.datagenfx;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GenerateUpdateJson {

    private final static String pointer = ">";

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        if (args == null || args.length == 0) {
            System.err.println("No mode specified, running manual mode");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runManual(scanner);
                }
            }, 3000);
        } else {
            if(args[0].equals("manual")) {
                runManual(scanner);
            } else if(args[0].equals("auto")) {
                if(!runAutomatic(scanner)) {
                    runManual(scanner);
                }
            }
        }
    }

    private static void runManual(Scanner scanner) {
        System.out.println("Manual mode running...");
        System.out.println();
        System.out.println("Version:");
        var version = scanner.nextLine().trim();

        System.out.println();
        System.out.println("Release type: ");
        String status = null;
        do {
            var nl = scanner.nextLine().toUpperCase().trim();
            if (nl.equals("ALPHA") || nl.equals("BETA") || nl.equals("PRERELEASE") || nl.equals("RELEASE")) {
                status = nl;
            } else System.err.println("Invalid type, allowed types: ALPHA, BETA, PRERELEASE, RELEASE");
        } while (status == null);
        System.out.println();
        System.out.println("Release date (empty line = current time):");
        String date;

        //--------DATA INPUT--------\\
        var dl = scanner.nextLine().toUpperCase().trim();
        var d = Calendar.getInstance();
        if (dl.equals("")) {
            date = String.format("%s.%s.%s.%s",
                    d.get(Calendar.YEAR),
                    d.get(Calendar.MONTH) + 1,
                    d.get(Calendar.DAY_OF_MONTH),
                    d.get((Calendar.HOUR_OF_DAY)));
        } else {
            if (dl.split("\\.").length == 4) {
                date = dl;
            } else {
                System.err.println("Invalid date, enter date manually");
                try {
                    System.out.println("Year:");
                    var year = scanner.nextInt();
                    System.out.println("Month:");
                    var month = scanner.nextInt();
                    System.out.println("Day:");
                    var day = scanner.nextInt();
                    System.out.println("Hour:");
                    var hour = scanner.nextInt();
                    date = String.format("%s.%s.%s.%s", year, month, day, hour);
                } catch (InputMismatchException ignored) {
                    System.err.println("Invalid value, using current data");
                    date = String.format("%s.%s.%s.%s",
                            d.get(Calendar.YEAR),
                            d.get(Calendar.MONTH) + 1,
                            d.get(Calendar.DAY_OF_MONTH),
                            d.get((Calendar.HOUR_OF_DAY)));
                }
            }
        }

        System.out.println();
        String changelog = generateChangelist(scanner);
        System.out.printf("Ver: %s\nVerstat: %s\nDate: %s\nChangelog: %s",
                version,
                status,
                date,
                changelog);
        generateJson(new UpdateData(version, status, date, changelog), scanner);
    }

    private static boolean runAutomatic(Scanner scanner) {
        var prop = new Properties();
        try {
            prop.load(GenerateUpdateJson.class.getResourceAsStream("start.properties"));
        } catch (IOException e) {
            System.err.println("Load error, starting manual mode");
            return false;
        }
        var data = prop.getProperty("version").split(" ");
        String version, status, date, changelist;

        if(data.length == 2) {
            version = data[1];
            status = data[0];
        } else {
            version = data[0];
            status = "RELEASE";
        }

        var d = Calendar.getInstance();
        date = String.format("%s.%s.%s.%s",
                d.get(Calendar.YEAR),
                d.get(Calendar.MONTH) + 1,
                d.get(Calendar.DAY_OF_MONTH),
                d.get((Calendar.HOUR_OF_DAY)));

        changelist = generateChangelist(scanner);

        System.out.printf("Ver: %s\nVerstat: %s\nDate: %s\nChangelog: %s",
                version,
                status,
                date,
                changelist);

        generateJson(new UpdateData(version, status, date, changelist), scanner);
        return true;
    }

    private static String generateChangelist(Scanner scanner) {
        System.out.println("""
                Changelog manager commands:
                /removeLast - removing last entry
                /build - build changelog
                /clear - clear changelog
                /preview - show changelog preview
                """);
        LinkedList<String> changelogList = new LinkedList<>();
        var cl = "";
        build: do {
            System.out.print("> ");
            cl = scanner.nextLine().trim();
            switch (cl.toLowerCase()) {
                case "/removelast":
                    if(!(changelogList.toArray().length == 0)) {
                        if(changelogList.toArray().length == 1) {
                            changelogList.clear();
                        } else {
                            changelogList.removeLast();
                        }
                    } else System.err.println("Nothing to delete");
                    continue;
                case "/build":
                    break build;
                case "/clear":
                    changelogList.clear();
                    continue;
                case "/preview":
                    for (String entry : changelogList) {
                        System.out.println(pointer + " " + entry);
                    }
                    continue;
            }

            changelogList.add(cl);

        } while (true);

        var sb = new StringBuilder();
        sb.append("\n");
        for (String entry : changelogList) {
            sb.append(pointer + " ").append(entry).append("\n");
        }
        return sb.toString();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void generateJson(UpdateData data, Scanner scanner) {
        System.out.println("Write result to file? Write Y to yes");
        if(scanner.nextLine().equalsIgnoreCase("Y")) {
            try {
                File file = new File("./update.json");
                if (!file.exists()) file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(data));
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

record UpdateData(String version, String status, String date, String changelog) {}