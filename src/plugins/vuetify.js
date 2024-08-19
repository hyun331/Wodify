import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { VTimePicker } from "vuetify/labs/VTimePicker";
import {mdi} from 'vuetify/iconsets/mdi';

export default createVuetify({
	icons: {
        defaultSet: 'mdi',
        sets:{
            mdi,
        }
    },
	components: {
		...components, // 기존의 모든 컴포넌트 포함
		VTimePicker, // VTimePicker 추가
	},
	directives,
});