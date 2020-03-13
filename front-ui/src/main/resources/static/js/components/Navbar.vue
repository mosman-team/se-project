<template>
    <nav>
        <v-app-bar app flat>
            <v-app-bar-nav-icon class="grey--text" @click="drawer = !drawer" v-if="currentUser"></v-app-bar-nav-icon>
            <v-toolbar-title class="text-uppercase grey--text">
                <span class="font-weight-light">Tutor</span><span>Finder</span>
            </v-toolbar-title>
            <v-spacer></v-spacer>

            <div class="text-center" v-if="currentUser">
                <v-menu offset-y>
                    <template v-slot:activator="{ on }">
                        <v-btn
                                text
                                color="grey"
                                v-on="on">
                            <v-icon left>expand_more</v-icon>
                            <span>Menu</span>
                        </v-btn>
                    </template>
                    <v-list>
                        <v-list-item v-for="link in links" :key="link.text" router :to="link.route">

                            <v-list-item-title>
                                {{ link.text }}
                            </v-list-item-title>

                        </v-list-item>
                    </v-list>
                </v-menu>
            </div>

            <v-btn text color="grey" v-if="currentUser" @click="logOut">
                <span>Sign Out</span>
                <v-icon right>exit_to_app</v-icon>
            </v-btn>

            <v-btn text color="grey" v-if="!currentUser" router :to="'/login'">
                <span>Sign In</span>
            </v-btn>
            <v-btn text color="grey" v-if="!currentUser" router :to="'/register'">
                <span>Sign Up</span>
            </v-btn>

        </v-app-bar>

        <v-navigation-drawer app v-model="drawer" class="primary" absolute v-if="currentUser">
            <v-layout column align-center>
                <v-flex class="mt-5">
                    <v-avatar size="100">
                        <img src="/images/avatar.png">
                    </v-avatar>
                    <p class="white--text subheading mt-1">
                        {{currentUser.username}}
                    </p>
                </v-flex>


            </v-layout>

            <v-list>
                <v-list-item v-for="link in links" :key="link.text" router :to="link.route">
                    <v-list-item-icon>
                        <v-icon class="white--text">{{ link.icon }}</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                        <v-list-item-title class="white--text">{{ link.text }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>

        </v-navigation-drawer>

    </nav>
</template>

<script>
    export default {

        data(){
            return {
                drawer : false,
                links : [
                    {icon : 'dashboard', text : 'Dashboard', route : '/'},
                    {icon : 'folder', text : 'My Projects', route : '/projects'},
                    {icon : 'person', text : 'Team', route : '/team'},
                ]
            }
        },
        computed: {
            currentUser() {
                return this.$store.state.auth.user;
            }
        },
        methods: {
            logOut() {
                this.$store.dispatch('auth/logout');
                this.$router.push('/login');
            },
            goHome(){
                this.$router.push('/home');
            },
            showProfile() {
                this.$router.push('/profile');
            }
        }
    }
</script>

<style>

</style>