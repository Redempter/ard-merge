package haven;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import haven.Coord;
import haven.IButton;
import haven.LocalMiniMap;
import haven.DefSettings;


public class MinimapWnd extends ResizableWnd {
    private LocalMiniMap minimap;
    private final int header;
    public static Tex biometex;
    private boolean minimized;
    private Coord szr;
    public MapWnd mapfile;


    public MinimapWnd(final LocalMiniMap mm) {
        super(Coord.z, (Resource.getLocString(Resource.BUNDLE_WINDOW, "Minimap")));
        this.minimap = mm;
        final int spacer = 5;

        makeHidable();

        final ToggleButton2 pclaim = add(new ToggleButton2("gfx/hud/wndmap/btns/claim", "gfx/hud/wndmap/btns/claim-d", DefSettings.SHOWPCLAIM.get()) {
            {
                tooltip = Text.render(Resource.getLocString(Resource.BUNDLE_LABEL, "Display personal claims"));
            }

            public void click() {
                if ((ui.gui.map != null) && !ui.gui.map.visol(0)) {
                    ui.gui.map.enol(0, 1);
                    DefSettings.SHOWPCLAIM.set(true);
                } else {
                    ui.gui.map.disol(0, 1);
                    DefSettings.SHOWPCLAIM.set(false);
                }
            }
        },new Coord(0,0));
       final ToggleButton2 vclaim = add(new ToggleButton2("gfx/hud/wndmap/btns/vil", "gfx/hud/wndmap/btns/vil-d", DefSettings.SHOWVCLAIM.get()) {
            {
                tooltip = Text.render(Resource.getLocString(Resource.BUNDLE_LABEL, "Display village claims"));
            }

            public void click() {
                if ((ui.gui.map != null) && !ui.gui.map.visol(2)) {
                    ui.gui.map.enol(2, 3);
                    DefSettings.SHOWVCLAIM.set(true);
                } else {
                    ui.gui.map.disol(2, 3);
                    DefSettings.SHOWVCLAIM.set(false);
                }
            }
        },pclaim.c.add(pclaim.sz.x+spacer,0));
        final ToggleButton2 realm = add(new ToggleButton2("gfx/hud/wndmap/btns/realm", "gfx/hud/wndmap/btns/realm-d",    DefSettings.SHOWKCLAIM.get()) {
            {
                tooltip = Text.render(Resource.getLocString(Resource.BUNDLE_LABEL, "Display realms"));
            }

            public void click() {
                if ((ui.gui.map != null) && !ui.gui.map.visol(4)) {
                    ui.gui.map.enol(4, 5);
                    DefSettings.SHOWKCLAIM.set(true);
                } else {
                    ui.gui.map.disol(4, 5);
                    DefSettings.SHOWKCLAIM.set(false);
                }
            }
        },vclaim.c.add(vclaim.sz.x+spacer,0));
        final IButton mapwnd = add(new IButton("gfx/hud/wndmap/btns/map", "Open Map", () -> gameui().toggleMap()), realm.c.add(realm.sz.x + spacer,0));
        final IButton geoloc = new IButton("gfx/hud/wndmap/btns/geoloc", "", "", "") {
            @Override
            public Object tooltip(Coord c, Widget prev) {
                Pair<String, String> coords = getCurCoords();
                if (coords != null)
                    tooltip = Text.render(String.format("Current location: %s x %s", coords.a, coords.b));
                else
                    tooltip = Text.render("Unable to determine your current location.");
                return super.tooltip(c, prev);
            }

            @Override
            public void click() {
                Pair<String, String> coords = getCurCoords();
                if (coords != null) {
                    try {
                        WebBrowser.self.show(new URL(String.format("http://odditown.com/haven/map/#x=%s&y=%s&zoom=9", coords.a, coords.b)));
                    } catch (WebBrowser.BrowserException e) {
                        getparent(GameUI.class).error("Could not launch web browser.");
                    } catch (MalformedURLException e) {
                    }
                } else {
                    getparent(GameUI.class).error("Unable to determine your current location.");
                }
            }

            private Pair<String, String> getCurCoords() {
                return minimap.cur != null ? Config.gridIdsMap.get(minimap.cur.grid.id) : null;
            }
        };add(geoloc,mapwnd.c.add(mapwnd.sz.x + spacer,0));
        final IButton center = add(new IButton("gfx/hud/wndmap/btns/center", "Center map on player", () -> mm.center()),
                geoloc.c.add(geoloc.sz.x + spacer, 0));
        final IButton grid = add(new IButton("gfx/hud/wndmap/btns/grid", "Toggle grid on minimap", () -> gameui().toggleMapGrid()),
                center.c.add(center.sz.x + spacer, 0));
        final IButton viewdist = add(new IButton("gfx/hud/wndmap/btns/viewdist", "Toggle view range", () -> gameui().toggleMapViewDist()),
                grid.c.add(grid.sz.x + spacer, 0));

        header = pclaim.sz.y + spacer;
        add(mm, new Coord(0, header));
        pack();
    }

    @Override
    public void close()
    {
    //    hide();
        minimize();
    }

    @Override
    protected void added() {
        super.added();
        minimap.sz = asz.sub(0, header);
    }

    @Override
    public void resize(Coord sz) {
        super.resize(sz);
        minimap.sz = asz.sub(0, header);
    }

    @Override
    public void uimsg(String msg, Object... args) {
        if (msg == "pack") {
            pack();
        } else if (msg == "dt") {
            return;
        } else if (msg == "cap") {
            return;
        } else {
            super.uimsg(msg, args);
        }
    }

    @Override
    public Coord xlate(Coord c, boolean in) {
        if (in)
            return (c.add(tlm));
        else
            return (c.sub(tlm));
    }

    @Override
    public boolean mousedown(Coord c, int button) {
        if (!minimized && c.x > sz.x - 20 && c.y > sz.y - 15) {
            doff = c;
            dm = ui.grabmouse(this);
            resizing = true;
            return true;
        }

        if (!minimized) {
            parent.setfocus(this);
            raise();
        }

        if (super.mousedown(c, button)) {
            parent.setfocus(this);
            raise();
            return true;
        }

        if (c.isect(tlm, asz)) {
            if (button == 1) {
                dm = ui.grabmouse(this);
                doff = c;
            }
            parent.setfocus(this);
            raise();
            return true;
        }
        return false;
    }

    @Override
    public void mousemove(Coord c) {
        if (resizing && dm != null) {
            Coord d = c.sub(doff);
            doff = c;
            mmap.sz.x = Math.max(mmap.sz.x + d.x, minsz.x);
            mmap.sz.y = Math.max(mmap.sz.y + d.y, minsz.y);
            pack();
            Utils.setprefc("mmapwndsz", sz);
            Utils.setprefc("mmapsz", mmap.sz);
        } else {
            if (dm != null) {
                this.c = this.c.add(c.add(doff.inv()));
                if (this.c.x < 0)
                    this.c.x = 0;
                if (this.c.y < 0)
                    this.c.y = 0;
                Coord gsz = gameui().sz;
                if (this.c.x + sz.x > gsz.x)
                    this.c.x = gsz.x - sz.x;
                if (this.c.y + sz.y > gsz.y)
                    this.c.y = gsz.y - sz.y;
            } else {
                super.mousemove(c);
            }
        }
    }

    @Override
    public boolean mouseup(Coord c, int button) {
        resizing = false;

        if (dm != null) {
            Utils.setprefc("mmapc", this.c);
            dm.remove();
            dm = null;
        } else {
            return super.mouseup(c, button);
        }
        return true;
    }

    @Override
    public void wdgmsg(Widget sender, String msg, Object... args) {
        if (sender == cbtn) {
            minimize();
        } else {
            super.wdgmsg(sender, msg, args);
        }
    }

    @Override
    public boolean keydown(KeyEvent ev) {
        int key = ev.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            wdgmsg(cbtn, "click");
            return (true);
        }
        return (super.keydown(ev));
    }

    private void minimize() {
        minimized = !minimized;
        if (minimized) {
            this.minimap.hide();
        } else {
            this.minimap.show();
        }

        if (minimized) {
            szr = asz;
            resize(new Coord(asz.x, 24));
        } else {
            resize(szr);
        }
    }
}
