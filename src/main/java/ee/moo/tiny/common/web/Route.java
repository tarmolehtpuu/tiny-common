package ee.moo.tiny.common.web;

import ee.moo.tiny.common.util.StringUtil;

public record Route(String ns, String controller, String action, Long id) {

    public static final Route UNKNOWN = new Route(null, null, null, 0L);

    public Route(String ns, String controller, String action) {
        this(ns, controller, action, 0L);
    }

    public Route(String controller, String action, Long id) {
        this(null, controller, action, id);
    }

    public Route(String controller, String action) {
        this(null, controller, action, 0L);
    }

    public boolean isUnknown() {
        return UNKNOWN == this;
    }

    public static Route parse(String method, String path) {
        return parse(null, method, path);
    }

    public static Route parse(String ns, String method, String path) {
        if (!StringUtil.isEmpty(ns)) {
            if (path.startsWith(ns)) {
                path = path.substring(ns.length());
            }
        }

        var parts = StringUtil.split(path, "/");

        if (parts.isEmpty()) {
            return new Route(ns, "home", "index");
        }

        if (parts.size() == 1) {
            if (StringUtil.equalsIgnoreCase("POST", method)) {
                return new Route(ns, parts.get(0), "create");
            }

            if (StringUtil.equalsIgnoreCase("GET", method)) {
                return new Route(ns, parts.get(0), "index");
            }

            return Route.UNKNOWN;
        }
        if (parts.size() == 2) {
            if (StringUtil.equals("new", parts.get(1))) {
                if (StringUtil.equalsIgnoreCase("GET", method)) {
                    return new Route(ns, parts.get(0), "new");
                }

                return Route.UNKNOWN;
            }

            if (StringUtil.isNumber(parts.get(1))) {
                if (StringUtil.equalsIgnoreCase("GET", method)) {
                    return new Route(ns, parts.get(0), "show", Long.parseLong(parts.get(1)));
                }

                if (StringUtil.equalsIgnoreCase("PATCH", method) || StringUtil.equalsIgnoreCase("PUT", method)) {
                    return new Route(ns, parts.get(0), "update", Long.parseLong(parts.get(1)));
                }

                if (StringUtil.equalsIgnoreCase("DELETE", method)) {
                    return new Route(ns, parts.get(0), "delete", Long.parseLong(parts.get(1)));
                }

                return Route.UNKNOWN;
            }

            return new Route(ns, parts.get(0), parts.get(1));
        }

        if (parts.size() == 3) {
            return new Route(ns, parts.get(0), parts.get(2), Long.parseLong(parts.get(1)));
        }

        return UNKNOWN;
    }
}

