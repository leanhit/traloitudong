<template>
    <div class="SkeletonBox-rounded d-flex justify-center align-center flex-column align-self-center "
         style="background-color: #fff; width: 30rem; height: 20rem; justify-self: center;margin: auto;">
        <div class="d-flex justify-center align-center flex-column">
            <div class="position-relative d-flex">
                <!-- <img src="/static/image/rolling-loading-SkeletonBox.svg" 
                     class="m-auto"
                     style="width: 6rem;" /> -->
                <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" 
                    style="margin:auto;display:block;"
                    width="100px" height="100px" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid">
                    <defs>
                        <clipPath id="ldio-f02z4grarif-cp">
                            <rect x="0" y="0" width="100" height="50">
                                <animate attributeName="y" repeatCount="indefinite" dur="2.2222222222222223s" calcMode="spline"
                                    values="0;50;0;0;0" keyTimes="0;0.4;0.5;0.9;1"
                                    keySplines="0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7"></animate>
                                <animate attributeName="height" repeatCount="indefinite" dur="2.2222222222222223s" calcMode="spline"
                                    values="50;0;0;50;50" keyTimes="0;0.4;0.5;0.9;1"
                                    keySplines="0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7"></animate>
                            </rect>
                            <rect x="0" y="50" width="100" height="50">
                                <animate attributeName="y" repeatCount="indefinite" dur="2.2222222222222223s" calcMode="spline"
                                    values="100;50;50;50;50" keyTimes="0;0.4;0.5;0.9;1"
                                    keySplines="0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7"></animate>
                                <animate attributeName="height" repeatCount="indefinite" dur="2.2222222222222223s" calcMode="spline"
                                    values="0;50;50;0;0" keyTimes="0;0.4;0.5;0.9;1"
                                    keySplines="0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7;0.3 0 1 0.7"></animate>
                            </rect>
                        </clipPath>
                    </defs>
                    <g transform="translate(50 50)">
                        <g transform="scale(0.9)">
                            <g transform="translate(-50 -50)">
                                <g>
                                    <animateTransform attributeName="transform" type="rotate" dur="2.2222222222222223s"
                                        repeatCount="indefinite" values="0 50 50;0 50 50;180 50 50;180 50 50;360 50 50"
                                        keyTimes="0;0.4;0.5;0.9;1"></animateTransform>
                                    <path clip-path="url(#ldio-f02z4grarif-cp)" fill="#5699d2"
                                        d="M54.864 50L54.864 50c0-1.291 0.689-2.412 1.671-2.729c9.624-3.107 17.154-12.911 19.347-25.296 c0.681-3.844-1.698-7.475-4.791-7.475H28.908c-3.093 0-5.472 3.631-4.791 7.475c2.194 12.385 9.723 22.189 19.347 25.296 c0.982 0.317 1.671 1.438 1.671 2.729v0c0 1.291-0.689 2.412-1.671 2.729C33.84 55.836 26.311 65.64 24.117 78.025 c-0.681 3.844 1.698 7.475 4.791 7.475h42.184c3.093 0 5.472-3.631 4.791-7.475C73.689 65.64 66.16 55.836 56.536 52.729 C55.553 52.412 54.864 51.291 54.864 50z">
                                    </path>
                                    <path fill="#1d3f72"
                                        d="M81 81.5h-2.724l0.091-0.578c0.178-1.122 0.17-2.243-0.022-3.333C76.013 64.42 68.103 54.033 57.703 50.483l-0.339-0.116 v-0.715l0.339-0.135c10.399-3.552 18.31-13.938 20.642-27.107c0.192-1.089 0.2-2.211 0.022-3.333L78.276 18.5H81 c2.481 0 4.5-2.019 4.5-4.5S83.481 9.5 81 9.5H19c-2.481 0-4.5 2.019-4.5 4.5s2.019 4.5 4.5 4.5h2.724l-0.092 0.578 c-0.178 1.122-0.17 2.243 0.023 3.333c2.333 13.168 10.242 23.555 20.642 27.107l0.338 0.116v0.715l-0.338 0.135 c-10.4 3.551-18.31 13.938-20.642 27.106c-0.193 1.09-0.201 2.211-0.023 3.333l0.092 0.578H19c-2.481 0-4.5 2.019-4.5 4.5 s2.019 4.5 4.5 4.5h62c2.481 0 4.5-2.019 4.5-4.5S83.481 81.5 81 81.5z M73.14 81.191L73.012 81.5H26.988l-0.128-0.309 c-0.244-0.588-0.491-1.538-0.28-2.729c2.014-11.375 8.944-20.542 17.654-23.354c2.035-0.658 3.402-2.711 3.402-5.108 c0-2.398-1.368-4.451-3.403-5.108c-8.71-2.812-15.639-11.979-17.653-23.353c-0.211-1.191 0.036-2.143 0.281-2.731l0.128-0.308 h46.024l0.128 0.308c0.244 0.589 0.492 1.541 0.281 2.731c-2.015 11.375-8.944 20.541-17.654 23.353 c-2.035 0.658-3.402 2.71-3.402 5.108c0 2.397 1.368 4.45 3.403 5.108c8.71 2.812 15.64 11.979 17.653 23.354 C73.632 79.651 73.384 80.604 73.14 81.191z">
                                    </path>
                                </g>
                            </g>
                        </g>
                    </g>
                </svg>
            </div>
            <div class="text-center p-3">
                <h5 class="text-info">
                    {{randomMessage}}
                </h5>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    export default {
        name: "SkeletonBox",
        props: {
            maxWidth: {
                default: 100,
                type: Number,
            },
            minWidth: {
                default: 80,
                type: Number,
            },
            height: {
                default: `1em`,
                type: String,
            },
            width: {
                default: null,
                type: String,
            },
        },
        data() {
            return {
                loadingMessage: [
                    "Please be patient, we're keep working...",
                    'How about this weather, eh?',
                    'Building a wall...',
                    'Running with scissors...',
                    'Work, work...',
                    'Patience! This is difficult, you know...',
                    "Please wait while the minions do their work",
                    "Grabbing extra minions",
                    "Doing the heavy lifting",
                    "We're working very Hard .... Really",
                    "Waking up the minions"
                ]
            };
        },
        computed: {
            computedWidth() {
                return this.width || `${Math.floor((Math.random() * (this.maxWidth - this.minWidth)) + this.minWidth)}%`;
            },
            randomMessage() {
                const randomElement = this.loadingMessage[Math.floor(Math.random() * this.loadingMessage.length)];
                return randomElement;
            }
        },
    };
</script>

<style lang="css">
    .SkeletonBox {
        display: inline-block;
        position: relative;
        vertical-align: middle;
        overflow: hidden;
        background-color: #dddbdd;
    }

    .SkeletonBox-rounded {
        -webkit-border-radius: 6px;
        -moz-border-radius: 6px;
        border-radius: 6px;
    }

    .SkeletonBox::after {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        transform: translateX(-100%);
        background-image: linear-gradient(90deg, rgba(255, 255, 255, 0) 0, rgba(255, 255, 255, 0.2) 20%, rgba(255, 255, 255, 0.5) 60%, rgba(255, 255, 255, 0));
        animation: shimmer 5s infinite;
        content: '';
    }

    @keyframes shimmer {
        100% {
            transform: translateX(100%);
        }
    }
</style>