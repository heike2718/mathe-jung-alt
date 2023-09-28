
export class Configuration {
    constructor(
        public baseUrl: string,
        public assetsPath: string,
        public withCredentials: boolean,
        public clientId: string,
        public production: boolean
    ) { }
};
