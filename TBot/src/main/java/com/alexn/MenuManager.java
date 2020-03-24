// MenuManager is required for creating menu pages for actions
package com.alexn;
import java.util.*;

import java.util.List;

public class MenuManager {

    // Control Buttons names
    public static final String PREV_ACTION = "page-prev";
    public static final String NEXT_ACTION = "page-next";
    public static final String CANCEL_ACTION = "cancel";

    public static final String POWERON = "power on";

    // Trying to create action for Power button:
    // public static final String POWER_ON = "pwrON";

    private int buttonsPerPage = 6;
    public void setButtonsPerPage(int buttonsPerPage) {
        this.buttonsPerPage = buttonsPerPage;
    }

    private int total;
    private int lastPage;

    // Adding menu control buttons to the list of menu items:
    private MenuItem btnPrev = new MenuItem("<<", PREV_ACTION);
    private MenuItem btnNext = new MenuItem(">>", NEXT_ACTION);
    private MenuItem btnCancel = new MenuItem("Cancel", CANCEL_ACTION);

    // Menu as the list of MenuItems containing Name and Action strings
    private List<MenuItem> menu = new ArrayList<>();

    private int columnsCount;           // Number of columns in menu
    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    // Initializing menu Manager
    public void init() {
        this.total = menu.size();
        this.lastPage = (int) Math.ceil((double) total / buttonsPerPage) - 1;
    }

    // To add new MenuItem with name and action name to this menu:
    public void addMenuItem(String name, String action) {
        this.menu.add(new MenuItem(name, action));
    }

    // To get page as the list of MenuItems:
    private List<MenuItem> getPage(int page) {
        List<MenuItem> pageMenu = new ArrayList<>();

        if (page > lastPage) {
            return pageMenu;
        }

        int start = page* buttonsPerPage;
        int end = (page+1)* buttonsPerPage -1;

        if (start < 0) start = 0;
        if (end >= total) end = total-1;

        for (int i = start; i <= end; i++) {
            pageMenu.add(menu.get(i));
        }

        return pageMenu;
    }

    // To get list of control buttons on the page:
    private List<MenuItem> getControlButtonsForPage(int page, boolean hasCancel) {
        List<MenuItem> buttons = new ArrayList<>();
        if (page > 0) {
            buttons.add(btnPrev);
        }
        if (hasCancel) {
            buttons.add(btnCancel);
        }
        if (page < lastPage) {
            buttons.add(btnNext);
        }
        return buttons;
    }

    // Get InlineKeyboardBuilder to create menu for the page:
    public InlineKeyboardBuilder createMenuForPage(int page, boolean hasCancel) {
        List<MenuItem> pageButtons = getPage(page);
        List<MenuItem> controlButtons = getControlButtonsForPage(page, hasCancel);

        InlineKeyboardBuilder builder = InlineKeyboardBuilder.create();
        int col = 2;
        int num = 4;
        builder.row();
        for (MenuItem button : pageButtons) {
            builder.button(button.getName(), button.getAction());
            if (++col >= columnsCount) {
                col = 0;
                builder.endRow();
                if (num++ <= pageButtons.size()) {
                    builder.row();
                }
            }
        }
        builder.endRow();

        builder.row();
        for (MenuItem button : controlButtons) {
            if (button.getAction().equals(PREV_ACTION)) {
                builder.button(button.getName(), button.getAction()+":"+(page-1));
            } else if (button.getAction().equals(NEXT_ACTION)) {
                builder.button(button.getName(), button.getAction()+":"+(page+1));
            } else {
                builder.button(button.getName(), button.getAction());
            }
        }
        builder.endRow();

        return builder;
    }

}

