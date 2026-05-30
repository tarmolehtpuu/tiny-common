package ee.moo.tiny.common.web;

import ee.moo.tiny.common.util.StringUtil;

public record Route(String controller, String action, Long id) {

    public static final Route UNKNOWN = new Route(null, null, 0L);

    public boolean isUnknown() {
        return UNKNOWN == this;
    }

    public static Route parse(String method, String path) {
        var parts = StringUtil.split(path, "/");

        if (parts.isEmpty()) {
            return new Route("home", "index", 0L);
        }

        if (parts.size() == 1) {
            if (StringUtil.equalsIgnoreCase("POST", method)) {
                return new Route(parts.get(0), "create", 0L);
            }

            if (StringUtil.equalsIgnoreCase("GET", method)) {
                return new Route(parts.get(0), "index", 0L);
            }

            return Route.UNKNOWN;
        }
        if (parts.size() == 2) {
            if (StringUtil.equals("new", parts.get(1))) {
                if (StringUtil.equalsIgnoreCase("GET", method)) {
                    return new Route(parts.get(0), "new", 0L);
                }

                return Route.UNKNOWN;
            }

            if (StringUtil.isNumber(parts.get(1))) {
                if (StringUtil.equalsIgnoreCase("GET", method)) {
                    return new Route(parts.get(0), "show", Long.parseLong(parts.get(1)));
                }

                if (StringUtil.equalsIgnoreCase("PATCH", method) || StringUtil.equalsIgnoreCase("PUT", method)) {
                    return new Route(parts.get(0), "update", Long.parseLong(parts.get(1)));
                }

                if (StringUtil.equalsIgnoreCase("DELETE", method)) {
                    return new Route(parts.get(0), "delete", Long.parseLong(parts.get(1)));
                }

                return Route.UNKNOWN;
            }

            return new Route(parts.get(0), parts.get(1), 0L);
        }

        if (parts.size() == 3) {
            return new Route(parts.get(0), parts.get(2), Long.parseLong(parts.get(1)));
        }

        return UNKNOWN;
    }
}

