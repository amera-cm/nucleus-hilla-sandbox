import {ConnectClient} from "@vaadin/hilla-frontend";
import {LogMiddleware} from "Frontend/middleware/log-mw.js";

const client = new ConnectClient({prefix: "uiops", middlewares: [LogMiddleware]});
export default client;
