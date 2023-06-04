
export class Configuration {
    constructor(
        public baseUrl: string,
        public assetsPath: string,
        public clientType: string,
        public withCredentials: boolean,
        public clientId: string
    ) { }
};
