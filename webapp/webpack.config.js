const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const PATHS = {
    entry: path.resolve(__dirname, 'src/index.tsx'),
    htmlEntry: path.resolve(__dirname, 'src/index.html'),
    src: path.resolve(__dirname, 'src'),
    dist: path.resolve(__dirname, 'dist')
};

module.exports = {
    entry: PATHS.entry,
    output: {
        path: PATHS.dist,
        filename: '[name].[hash].js',
        publicPath: '/'
    },
    resolve: {
        alias: {'@src': PATHS.src},
        extensions: ['.ts', '.tsx', '.js', '.jsx', '.json', '.css'],
        modules: ['src', 'node_modules'],
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loader: "ts-loader"
            },
            // rule for custom css which is imported as module
            {
                test: /\.css$/i,
                exclude: /node_modules/,
                use: ['style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true
                        },
                    }]
            },
            // rule for bootstrap css which is not imported as module
            {
                test: /\.css$/i,
                include: /node_modules/,
                use: ['style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            modules: false
                        }
                    }]
            },
            // rule for pictures
            {
                test: /\.(png|jp(e*)g|svg|gif)$/,
                use: [
                    {
                        loader: 'file-loader',
                        options: {
                            name: 'images/[hash]-[name].[ext]',
                        },
                    },
                ],
            }
        ],
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: PATHS.htmlEntry,
            favicon: "./assets/favicons/favicon.ico"
        }),
    ],
    devServer: {
        port: 3000,
        historyApiFallback: {
            disableDotRule: true,
        },
        proxy: {
            '/api': 'http://localhost:8080'
        }
    },
};