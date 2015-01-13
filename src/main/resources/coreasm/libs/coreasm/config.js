require.config({
    paths: {
        "coreasm": "/bms/libs/coreasm/coreasm"
    },
    shim: {
        'coreasm': ['bms']
    },
    deps: ['/bms/libs/bmotion/config.js']
})