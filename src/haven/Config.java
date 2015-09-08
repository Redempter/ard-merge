/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import haven.error.ErrorHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static haven.Utils.getprop;

public class Config {
    public static String authuser = getprop("haven.authuser", null);
    public static String authserv = getprop("haven.authserv", null);
    public static String defserv = getprop("haven.defserv", "127.0.0.1");
    public static URL resurl = geturl("haven.resurl", "");
    public static URL mapurl = geturl("haven.mapurl", "");
    public static boolean dbtext = getprop("haven.dbtext", "off").equals("on");
    public static boolean bounddb = getprop("haven.bounddb", "off").equals("on");
    public static boolean profile = getprop("haven.profile", "off").equals("on");
    public static boolean profilegpu = getprop("haven.profilegpu", "off").equals("on");
    public static boolean fscache = getprop("haven.fscache", "on").equals("on");
    public static String resdir = getprop("haven.resdir", null);
    public static boolean nopreload = getprop("haven.nopreload", "no").equals("yes");
    public static String loadwaited = getprop("haven.loadwaited", null);
    public static String allused = getprop("haven.allused", null);
    public static int mainport = getint("haven.mainport", 1870);
    public static int authport = getint("haven.authport", 1871);
    public static boolean softres = getprop("haven.softres", "on").equals("on");

    public static boolean showflo = Utils.getprefb("showflo", true);
    public static boolean daylight = Utils.getprefb("daylight", false);
    public static boolean showkinnames = Utils.getprefb("showkinnames", false);
    public static boolean showplayersmmap = Utils.getprefb("showplayersmmap", false);
    public static boolean savemmap = Utils.getprefb("savemmap", true);
    public static boolean studylock = Utils.getprefb("studylock", false);
    public static boolean camfree = Utils.getprefb("camfree", false);
    public static boolean chatsave = Utils.getprefb("chatsave", false);
    public static boolean chattimestamp = Utils.getprefb("chattimestamp", false);
    public static boolean alarmunknown = Utils.getprefb("alarmunknown", false);
    public static double alarmunknownvol = Utils.getprefd("alarmunknownvol", 0.32);
    public static boolean alarmred = Utils.getprefb("alarmred", false);
    public static double alarmredvol = Utils.getprefd("alarmredvol", 0.32);
    public static boolean showquality = Utils.getprefb("showquality", false);
    public static int showqualitymode = Utils.getprefi("showqualitymode", 0);
    public static int badcamsensitivity = Utils.getprefi("badcamsensitivity", 5);
    public static List<LoginData> logins = new ArrayList<LoginData>();
    public static boolean maplocked = Utils.getprefb("maplocked", false);
    public static boolean mapwndlocked = Utils.getprefb("mapwndlocked", false);
    public static boolean disabletiletrans = Utils.getprefb("disabletiletrans", false);
    public static boolean itemmeterbar = Utils.getprefb("itemmeterbar", false);

    public static byte[] authck = null;
    public static String prefspec = "hafen";
    public static String version;

    public final static String chatfile = "chatlog.txt";
    public static PrintWriter chatlog = null;

    public final static String[] boulders = new String[]{"basalt", "limonite", "schist", "dolomite", "magnetite", "gneiss",
            "granite", "malachite", "hematite", "porphyry", "ilmenite", "quartz", "cassiterite", "limestone", "sandstone",
            "chalcopyrite", "cinnabar", "feldspar", "marble", "ras", "flint", "hornsilver"};
    public static String[] boulderssel = Utils.getprefsa("boulderssel", null);

    public final static String[] bushes = new String[]{"arrowwood", "crampbark", "sandthorn", "blackberrybush", "dogrose",
            "spindlebush", "blackcurrant", "elderberrybush", "teabush", "blackthorn", "gooseberrybush", "tibast",
            "bogmyrtle", "hawthorn", "tundrarose", "boxwood", "holly", "woodbine", "bsnightshade", "raspberrybush",
            "caprifole", "redcurrant"};
    public static String[] bushessel = Utils.getprefsa("bushessel", null);

    public final static String[] trees = new String[]{"alder", "corkoak", "plumtree", "juniper", "crabappletree", "kingsoak",
            "oak", "walnuttree", "birdcherrytree", "larch", "poplar", "whitebeam", "appletree", "cypress", "buckthorn",
            "laurel", "ash", "elm", "rowan", "willow", "cedar", "linden", "olivetree", "aspen",  "fir", "baywillow",
            "goldenchain", "peartree", "sallow", "yew", "cherry", "maple", "beech", "chestnuttree", "hazel", "spruce",
            "hornbeam", "mirkwood", "conkertree", "mulberry", "sweetgum", "pine", "birch", "planetree"};
    public static String[] treessel = Utils.getprefsa("treessel", null);

    static {
        Arrays.sort(Config.boulders);
        Arrays.sort(Config.bushes);
        Arrays.sort(Config.trees);

        String p;
        if ((p = getprop("haven.authck", null)) != null)
            authck = Utils.hex2byte(p);

        try {
            InputStream in = ErrorHandler.class.getResourceAsStream("/version");
            try {
                if (in != null) {
                    java.util.Scanner s = new java.util.Scanner(in);
                    version = s.next();
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {}

        loadLogins();
    }

    private static void loadLogins() {
        try {
            String loginsjson = Utils.getpref("logins", null);
            if (loginsjson == null)
                return;
            JSONArray larr = new JSONArray(loginsjson);
            for (int i = 0; i < larr.length(); i++) {
                JSONObject l = larr.getJSONObject(i);
                logins.add(new LoginData(l.get("name").toString(), l.get("pass").toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveLogins() {
        try {
            List<String> larr = new ArrayList<String>();
            for (LoginData ld : logins) {
                String ldjson = new JSONObject(ld, new String[] {"name", "pass"}).toString();
                larr.add(ldjson);
            }
            String jsonobjs = "";
            for (String s : larr)
                jsonobjs += s + ",";
            if (jsonobjs.length() > 0)
                jsonobjs = jsonobjs.substring(0, jsonobjs.length()-1);
            Utils.setpref("logins", "[" + jsonobjs + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getint(String name, int def) {
        String val = getprop(name, null);
        if (val == null)
            return (def);
        return (Integer.parseInt(val));
    }

    private static URL geturl(String name, String def) {
        String val = getprop(name, def);
        if (val.equals(""))
            return (null);
        try {
            return (new URL(val));
        } catch (java.net.MalformedURLException e) {
            throw (new RuntimeException(e));
        }
    }

    private static void usage(PrintStream out) {
        out.println("usage: haven.jar [OPTIONS] [SERVER[:PORT]]");
        out.println("Options include:");
        out.println("  -h                 Display this help");
        out.println("  -d                 Display debug text");
        out.println("  -P                 Enable profiling");
        out.println("  -G                 Enable GPU profiling");
        out.println("  -U URL             Use specified external resource URL");
        out.println("  -r DIR             Use specified resource directory (or HAVEN_RESDIR)");
        out.println("  -A AUTHSERV[:PORT] Use specified authentication server");
        out.println("  -u USER            Authenticate as USER (together with -C)");
        out.println("  -C HEXCOOKIE       Authenticate with specified hex-encoded cookie");
    }

    public static void cmdline(String[] args) {
        PosixArgs opt = PosixArgs.getopt(args, "hdPGU:r:A:u:C:");
        if (opt == null) {
            usage(System.err);
            System.exit(1);
        }
        for (char c : opt.parsed()) {
            switch (c) {
                case 'h':
                    usage(System.out);
                    System.exit(0);
                    break;
                case 'd':
                    dbtext = true;
                    break;
                case 'P':
                    profile = true;
                    break;
                case 'G':
                    profilegpu = true;
                    break;
                case 'r':
                    resdir = opt.arg;
                    break;
                case 'A':
                    int p = opt.arg.indexOf(':');
                    if (p >= 0) {
                        authserv = opt.arg.substring(0, p);
                        authport = Integer.parseInt(opt.arg.substring(p + 1));
                    } else {
                        authserv = opt.arg;
                    }
                    break;
                case 'U':
                    try {
                        resurl = new URL(opt.arg);
                    } catch (java.net.MalformedURLException e) {
                        System.err.println(e);
                        System.exit(1);
                    }
                    break;
                case 'u':
                    authuser = opt.arg;
                    break;
                case 'C':
                    authck = Utils.hex2byte(opt.arg);
                    break;
            }
        }
        if (opt.rest.length > 0) {
            int p = opt.rest[0].indexOf(':');
            if (p >= 0) {
                defserv = opt.rest[0].substring(0, p);
                mainport = Integer.parseInt(opt.rest[0].substring(p + 1));
            } else {
                defserv = opt.rest[0];
            }
        }
    }

    static {
        Console.setscmd("stats", new Console.Command() {
            public void run(Console cons, String[] args) {
                dbtext = Utils.parsebool(args[1]);
            }
        });
        Console.setscmd("profile", new Console.Command() {
            public void run(Console cons, String[] args) {
                if (args[1].equals("none") || args[1].equals("off")) {
                    profile = profilegpu = false;
                } else if (args[1].equals("cpu")) {
                    profile = true;
                } else if (args[1].equals("gpu")) {
                    profilegpu = true;
                } else if (args[1].equals("all")) {
                    profile = profilegpu = true;
                }
            }
        });
    }
}
