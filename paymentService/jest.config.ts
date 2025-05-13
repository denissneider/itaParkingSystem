import type { Config } from 'jest';

const config: Config = {
  rootDir: '.',
  testMatch: ['**/test/**/*.spec.ts'],
  transform: {
    '^.+\\.ts$': 'ts-jest',
  },
  moduleFileExtensions: ['ts', 'js', 'json'],
  testEnvironment: 'node',
};

export default config;
