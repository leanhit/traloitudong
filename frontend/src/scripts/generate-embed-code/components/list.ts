import { useI18n } from 'vue-i18n';
import { ref, computed, watch } from 'vue';

export default {
    props: ['viewSettings'],
    setup() {
        const { t } = useI18n();
        const BOTPRESS_SERVER_URL = 'https://bot.traloitudong.com'; // Cập nhật nếu khác

        // Reactive state
        const botId = ref('');
        const activeSection = ref(''); // 'code' or 'test' or ''
        const copyStatusVisible = ref(false);
        const testIframeRef = ref<HTMLIFrameElement | null>(null);

        // Computed property for generated embed code
        const generatedCode = computed(() => {
            if (!botId.value) return '';
            return `
        <script src="${BOTPRESS_SERVER_URL}/assets/modules/channel-web/inject.js"><\/script>
        <script>
          window.botpressWebChat.init({
            botId: "${botId.value}",
            host: "${BOTPRESS_SERVER_URL}",
            chatId: "bp-web-widget",
            botName: "Trợ lý ảo",
            botConvoDescription: "Hỏi tôi bất kỳ điều gì",
            backgroundColor: "#ffffff",
            textColorOnBackground: "#000000"
          });
        <\/script>`;
        });

        // Function to generate full HTML content for the iframe
        const getBotpressFullHtmlContent = (id: string) => {
            return `
        <!DOCTYPE html>
        <html lang="vi">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Bot Test</title>
            <style>body { margin: 0; padding: 0; overflow: hidden; }</style>
        </head>
        <body>
            <p style="text-align: center; margin-top: 20px;">Đang tải bot...</p>
            ${getBotpressEmbedCodeScripts(id)}
        </body>
        </html>`;
        };

        // Helper function to get embed scripts (reused for iframe)
        const getBotpressEmbedCodeScripts = (id: string) => {
            return `
        <script src="${BOTPRESS_SERVER_URL}/assets/modules/channel-web/inject.js"><\/script>
        <script>
          window.botpressWebChat.init({
            botId: "${id}",
            host: "${BOTPRESS_SERVER_URL}",
            chatId: "bp-web-widget",
            botName: "Trợ lý ảo",
            botConvoDescription: "Hỏi tôi bất kỳ điều gì",
            backgroundColor: "#ffffff",
            textColorOnBackground: "#000000"
          });
        <\/script>`;
        };

        // Function to handle section display and content generation
        const showSection = (type: 'code' | 'test') => {
            if (!botId.value) {
                alert('Vui lòng nhập Bot ID trước!');
                return;
            }
            activeSection.value = type;
            copyStatusVisible.value = false; // Hide status when switching sections
        };

        // Watch for changes in activeSection to load iframe content
        watch(activeSection, (newSection) => {
            if (newSection === 'test' && testIframeRef.value) {
                const iframeContent = getBotpressFullHtmlContent(botId.value);
                const iframeDoc =
                    testIframeRef.value.contentDocument ||
                    testIframeRef.value.contentWindow?.document;
                if (iframeDoc) {
                    iframeDoc.open();
                    iframeDoc.write(iframeContent);
                    iframeDoc.close();
                }
            }
        });

        // Function to copy embed code to clipboard
        const copyEmbedCode = () => {
            if (!generatedCode.value) return;

            navigator.clipboard
                .writeText(generatedCode.value)
                .then(() => {
                    copyStatusVisible.value = true;
                    setTimeout(() => {
                        copyStatusVisible.value = false;
                    }, 2000);
                })
                .catch((err) => {
                    console.error('Failed to copy text: ', err);
                    alert(
                        'Không thể sao chép. Vui lòng thử lại hoặc sao chép thủ công.'
                    );
                });
        };

        return {
            t,
            botId,
            activeSection,
            copyStatusVisible,
            testIframeRef,
            generatedCode,
            showSection,
            copyEmbedCode,
            getBotpressFullHtmlContent,
            getBotpressEmbedCodeScripts,
        };
    },
};
