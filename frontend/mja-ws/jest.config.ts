import { getJestProjects } from '@nrwl/jest';

export default {
  projects: getJestProjects(),
};

module.exports = {
  transform: {
      '^.+\\.(ts|tsx)$': 'ts-jest',
  },
};
