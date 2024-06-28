import {Middleware, MiddlewareContext, MiddlewareNext} from '@vaadin/hilla-frontend';

// A middleware is an async function, that receives the `context` and `next`
export const LogMiddleware: Middleware = async function (
    context: MiddlewareContext,
    next: MiddlewareNext
) {
    // The context object contains the call arguments. See the `call` method
    // of the `ConnectClient` class for their descriptions.
    const {endpoint, method, params} = context;
    console.log(
        `Sending request to endpoint: ${endpoint} ` +
        `method: ${method} ` +
        `parameters: ${JSON.stringify(params)} `
    );

    // Also, the context contains the `request`, which is a Fetch API `Request`
    // instance to be sent over the network.
    const request: Request = context.request;
    console.log(`${request.method} ${request.url}`);

    // Call the `next` async function to send the request and get the response.
    // const response: Response = await next(context);

    // The response is a Fetch API `Response` object.
    // console.log(`Received response: ${response.status} ${response.statusText}`);

    // Clone the response if reading the body is necessary
    // const responseClone = response.clone();
    // console.log(await responseClone.text());

    // A middleware returns a response / continue chain.
    return next(context);
}