<template>
    <v-container
            fluid
            fill-height>
        <v-layout
                align-center
                justify-center>
            <v-flex
                    xs12
                    sm8
                    md4>
                <v-card class="elevation-12">
                    <v-toolbar
                            color="primary"
                            dark
                            flat>
                        <v-toolbar-title>Registration form</v-toolbar-title>
                        <v-spacer></v-spacer>
                    </v-toolbar>
                    <v-card-text>
                        <v-form ref="form"
                                v-model="valid">
                            <v-text-field
                                    v-model="user.username"
                                    :counter="10"
                                    :rules="nameRules"
                                    label="Username"
                                    required
                                    prepend-icon="person"
                            ></v-text-field>
                            <v-text-field
                                    prepend-icon="mdi-email"
                                    v-model="user.email"
                                    :rules="emailRules"
                                    label="E-mail"
                                    required>
                            </v-text-field>
                            <v-text-field
                                    autocomplete="current-password"
                                    :value="user.password"
                                    label="Enter password"
                                    required
                                    :append-icon="value ? 'mdi-eye' : 'mdi-eye-off'"
                                    @click:append="() => (value = !value)"
                                    :type="value ? 'password' : 'text'"
                                    :rules="passwordRules"
                                    @input="_=>user.password=_"
                                    prepend-icon="lock"
                            ></v-text-field>
                            <v-select
                                    prepend-icon="mdi-call-split"
                                    v-model="select"
                                    :items="items"
                                    :rules="[v => !!v || 'Item is required']"
                                    label="Student | Teacher"
                                    required>
                            </v-select>
                        </v-form>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                                :disabled="!valid"
                                color="primary"
                                class="mr-4"
                                @click="validate">
                            Sign up
                        </v-btn>
                        <v-btn
                                color="error"
                                class="mr-4"
                                @click="reset">
                            Reset Form
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
        name: 'Register',
        data() {
            return {
                // data and rules for text fields
                valid: true,
                nameRules: [
                    v => !!v || 'Name is required',
                    v => (v && v.length <= 10) || 'Name must be less than 10 characters',
                ],
                emailRules: [
                    v => !!v || 'E-mail is required',
                    v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
                ],
                passwordRules: [
                    v => !!v || 'Password is required',
                    v => ///^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/.test(v)
                        (v && v.length <= 10) || 'Min. 8 characters with at least one capital letter, a number and a special character.',
                ],
                value: true,
                select: null,
                items: [
                    'student',
                    'teacher',
                ],
                user: new User('', '', ''),
                // Position of registration form
                alignment: 'center',
                justify: 'center',
            };
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            }
        },
        mounted() {
            if (this.loggedIn) {
                this.$router.push('/profile');
            }
        },
        methods: {
            validate () {
                if (this.$refs.form.validate()) {
                    this.user.addRole(this.select);
                    this.$store.dispatch('auth/register', this.user).then(
                        data => {
                            this.$store.dispatch('setSnackbar', {text : data.message});
                            this.$router.push('/login')
                        },
                        error => {
                            this.res =
                                (error.response && error.response.data) ||
                                error.message ||
                                error.toString();
                            this.$store.dispatch('setSnackbar', {color : 'error',text : this.res.message});
                        }
                    );
                }
            },
            reset () {
                this.$refs.form.reset();
            }
        }
    };
</script>

<style scoped>

</style>