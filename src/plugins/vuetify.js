import "vuetify/styles";
import "vuetify/dist/vuetify.min.css";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { VTimePicker } from "vuetify/labs/VTimePicker";
import { VDataTable } from "vuetify/components";
import "@mdi/font/css/materialdesignicons.css";

export default createVuetify({
	icons: {
		iconfont: "mdi", // 기본값은 'mdi' (Material Design Icons)
	},
	components: {
		...components, // 기존의 모든 컴포넌트 포함
		VTimePicker, // VTimePicker 추가
		VDataTable,
	},
	directives,
});
