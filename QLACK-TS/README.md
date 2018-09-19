# QLACK TypeScript libraries
This is a collection of utility TypeScript libraries.

## Build & publish process
* Go to the root of the project you want to publish.
* Edit `package.json` to update the version.
* Go to the root of the QLACK-TS project and execute: `ng build --prod --project {projectname}`, e.g.
`ng build --prod --project @eurodyn/forms` 
* Go to the root of the QLACK-TS project under `dist` folder, e.g. `dist/eurodyn/forms` and execute: `npm publish`
