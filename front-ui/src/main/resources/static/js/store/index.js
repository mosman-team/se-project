import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';

Vue.use(Vuex);

export default new Vuex.Store({
    state : {
      snackbars : []
    },
    actions : {
        setSnackbar({commit}, snackbar){
            snackbar.showing = true;
            snackbar.timeout = snackbar.timeout || 6000;
            snackbar.color = snackbar.color || 'success';
            commit('SET_SNACKBAR', snackbar)
        }
    },
    mutations : {
        SET_SNACKBAR(state, snackbar){
            state.snackbars = state.snackbars.concat(snackbar);
        }
    },
    modules: {
        auth
    }
});