export class Requests {
    private static readonly API_ENDPOINT: string = "/api/";

    static async sendRequest(path: string,
                             options: any,
                             throwError: boolean = true): Promise<Response> {

        const response: Response = await fetch(this.API_ENDPOINT + path, options);

        if (throwError && !response.ok) {
            throw new Error(response.statusText);
        }

        return response;
    }
}
