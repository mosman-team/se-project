<template>
    <v-container
            fluid
            fill-height>
        <v-layout
                align-center
                justify-center
            >
            <v-flex
                    xs12
                    sm8
                    md4>
                <v-card class="elevation-12">
                    <v-toolbar
                            color="primary"
                            dark
                            flat>
                        <v-toolbar-title>Login form</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-card-text>
                        <v-form ref="form"
                                v-model="valid">
                            <v-text-field
                                    v-model="user.username"
                                    :counter="10"
                                    label="Username"
                                    :rules="nameRules"
                                    required
                                    prepend-icon="person"
                            ></v-text-field>
                            <v-text-field
                                    autocomplete="current-password"
                                    v-model="user.password"
                                    label="Enter password"
                                    required
                                    :rules="passwordRules"
                                    :append-icon="value ? 'mdi-eye' : 'mdi-eye-off'"
                                    @click:append="() => (value = !value)"
                                    :type="value ? 'password' : 'text'"
                                    @input="_=>user.password=_"
                                    prepend-icon="lock"
                            ></v-text-field>
                        </v-form>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                                :disabled="!valid"
                                color="primary"
                                class="mr-4"
                                @click="validate">
                            Login
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
    import User from '../../models/user';

    export default {
        name: 'Login',
        data() {
            return {
                user: new User('', ''),
                alignment: 'center',
                justify: 'center',
                valid : true,
                value : true,
                // login rules
                nameRules: [
                    v => !!v || 'Name is required'
                ],
                passwordRules: [
                    v => !!v || 'Password is required'
                ],
            };
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        created() {
            if (this.loggedIn) {
                this.$router.push('/profile');
            }
        },
        methods: {
            validate () {
                if (this.$refs.form.validate()) {
                    this.$store.dispatch('auth/login', this.user).then(
                        () => {
                            this.$store.dispatch('setSnackbar', {text : 'Successfully logged in!'});
                            this.$router.push('/profile');
                        },
                        error => {
                            this.res =
                                (error.response && error.response.data) ||
                                error.message ||
                                error.toString();
                            this.message = this.res.message || 'Incorrect username or password';
                            this.$store.dispatch('setSnackbar',
                                {color : 'error',text : this.message});
                        }
                    );
                }
            },
            reset () {
                this.$refs.form.reset();
            },
            resetValidation () {
                this.$refs.form.resetValidation()
            },
        }
    };
</script>

<style scoped>

</style>