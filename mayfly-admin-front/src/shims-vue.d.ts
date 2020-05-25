declare module "*.vue" {
    import Vue from "vue";
    export default Vue
}

// declare module "vue/types/vue" {
//     interface Vue {
//         $router: VueRouter; // 这表示this下有这个东西
//         $route: Route;
//         $Message: any;
//         $Permission: any;
//         $Utils: any;
//         $Config: any;
//     }
// }