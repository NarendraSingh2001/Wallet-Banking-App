module.exports = {
    collectCoverage: true,
    collectCoverageFrom: ['src/**/*.{js,jsx}'],
    coverageDirectory: 'coverage',
    testEnvironment: 'jsdom',
    setupFilesAfterEnv: ['<rootDir>/jest.setup.js'],
    transformIgnorePatterns: [
        "/node_modules/(?!react-toastify)/" 
      ],
      moduleNameMapper: {
        "\\.(css|less|sass|scss)$": "identity-obj-proxy" 
      },
}