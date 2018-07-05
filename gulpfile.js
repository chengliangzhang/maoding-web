/**
 * Created by Wuwq on 2016/10/20.
 */

var gulp = require('gulp');
var watch = require('gulp-watch');
var concat = require('gulp-concat');
var tmodjs = require('gulp-tmod');
var plumber = require('gulp-plumber');
var packageJSON = require('./package');
var jshintConfig = packageJSON.jshintConfig;
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var cssmin = require('gulp-clean-css');
var rename = require('gulp-rename');
/*var htmlReplace = require('gulp-html-replace');*/


/*-----------   生成 module  -----------*/
//编译模板JS
var compile_template = function () {
    return gulp.src('web2/src/main/webapp/assets/module/**/*.html')
        .pipe(plumber())
        .pipe(tmodjs({
            templateBase: 'web2/src/main/webapp/assets/module',
            helpers: 'web2/src/main/webapp/assets/module_template/template_func.js'
        }))
        .pipe(gulp.dest('web2/src/main/webapp/assets/module_template'));
};

//生成 module.css
var generate_module_css = function () {
    return gulp.src(['web2/src/main/webapp/assets/css/help.css',
        'web2/src/main/webapp/assets/css/custom.css',
        'web2/src/main/webapp/assets/module/**/*.css'])
        .pipe(plumber())
        .pipe(concat('module.css'))
        .pipe(gulp.dest('web2/src/main/webapp/assets/css'));
};

//生成 module.min.css
var generate_module_css_min = function () {
    return gulp.src('web2/src/main/webapp/assets/css/module.css')
        .pipe(plumber())
        .pipe(cssmin())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('web2/src/main/webapp/assets/css'));
};

//生成 其他 min.js
var generate_other_min_js = function () {
    return gulp.src(['web2/src/main/webapp/assets/js/app.js'
        , 'web2/src/main/webapp/assets/js/inspinia.js'
        , 'web2/src/main/webapp/assets/js/messageCenter.js'])
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('web2/src/main/webapp/assets/js'));
};

//生成 module.js
var generate_module_js = function () {
    return gulp.src([
        'web2/src/main/webapp/assets/js/restApi.js',
        'web2/src/main/webapp/assets/js/custom.js',
        'web2/src/main/webapp/assets/module_template/template.js',
        'web2/src/main/webapp/assets/module/**/*.js',
        'web2/src/main/webapp/assets/viewJs/*.js',
        'web2/src/main/webapp/assets/viewJs/**/*.js'])
        .pipe(plumber())
        .pipe(concat('module.js'))
        .pipe(gulp.dest('web2/src/main/webapp/assets/js'));
};

//生成 module.min.js
var generate_module_min_js = function () {
    return gulp.src('web2/src/main/webapp/assets/js/module.js')
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('web2/src/main/webapp/assets/js'));
};

gulp.task('generate_module', gulp.series(compile_template, generate_module_css, generate_module_css_min, generate_module_js, generate_other_min_js));

gulp.task('generate_module_min', gulp.series(compile_template, generate_module_css, generate_module_css_min, generate_module_js, generate_module_min_js, generate_other_min_js));


/*-----------   生成 VendorJS  -----------*/
//生成 vendor.js
var generate_vendor_js = function () {
    var jss = ['web2/src/main/webapp/assets/js/vendor_def.js',
        'web2/src/main/webapp/assets/lib/jquery-2x/jquery.js',
        'web2/src/main/webapp/assets/lib/underscore/underscore.js',
        'web2/src/main/webapp/assets/lib/underscore/underscore.string.js',
        'web2/src/main/webapp/assets/lib/moment/moment-with-locales.js',
        'web2/src/main/webapp/assets/js/uuid.js',
        'web2/src/main/webapp/assets/lib/js-cookie/js.cookie.js',
        'web2/src/main/webapp/assets/lib/json-js/json2.js'];

    return gulp.src(jss)
        .pipe(plumber())
        .pipe(concat('vendor.js'))
        .pipe(gulp.dest('web2/src/main/webapp/assets/js'));
};

//生成 vendor.min.js
var generate_vendor_min_js = function () {
    return gulp.src('web2/src/main/webapp/assets/js/vendor.js')
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('web2/src/main/webapp/assets/js'));
};

gulp.task('generate_vendor', gulp.series(generate_vendor_js, generate_vendor_min_js));



/*//生成 module.js（含JS代码检查）
 gulp.task('generate_module_js_with_error_check', ['compile_template'], function () {
 return gulp.src('web/src/main/webapp/assets/module/!**!/!*.js')
 .pipe(plumber())
 .pipe(jshint(jshintConfig))
 .pipe(jshint.reporter('default'))
 .pipe(concat('module.js'))
 .pipe(gulp.dest('web/src/main/webapp/assets/js'));
 });*/

/*
 //监听
 gulp.task('watch', ['generate_module_js'], function () {
 return watch('web/src/main/webapp/assets/module/!**!/!*.html', ['generate_module_js']);
 });
 */

/*
 gulp.task('default', ['clean'], function() {
 gulp.start('generate_module_js');
 });*/



/*var gulp = require('gulp'),
 image=require('gulp-image');*/


/*autoprefixer = require('gulp-autoprefixer'),
 minifycss = require('gulp-minify-css'),
 jshint = require('gulp-jshint'),
 uglify = require('gulp-uglify'),

 rename = require('gulp-rename'),
 clean = require('gulp-clean'),
 concat = require('gulp-concat'),
 notify = require('gulp-notify'),
 cache = require('gulp-cache')*/

/*gulp.task('img', function() {
 return  gulp.src('D:\\img\\*.{png,jpg,gif,ico}')
 .pipe(imagemin({
 optimizationLevel: 3, //类型：Number  默认：3  取值范围：0-7（优化等级）
 progressive: true, //类型：Boolean 默认：false 无损压缩jpg图片
 interlaced: true, //类型：Boolean 默认：false 隔行扫描gif进行渲染
 multipass: true //类型：Boolean 默认：false 多次优化svg直到完全优化
 }))
 .pipe(gulp.dest('dist'))
 .pipe(notify({ message: 'Images task complete' }));
 });*/

/*gulp.task('jpgmin',function(){
 return gulp.src('./app-work/src/main/webapp/common/img/!*')
 .pipe(imagemin({
 progressive: true,
 use:[jpegtran()]
 }))
 .pipe(gulp.dest('dist/'));
 });

 // 任务：压缩png
 gulp.task('pngmin',function(){
 return gulp.src('./app-work/src/main/webapp/common/img/!*')
 .pipe(imagemin({
 quality: '65-80',
 speed: 4,
 use:[pngquant()]
 }))
 .pipe(gulp.dest('dist/'));
 });*/


/*gulp.task('img',function(){
 return gulp.src('app-work/src/main/webapp/common/img/!*')
 .pipe(image({
 pngquant: true,
 optipng: false,
 zopflipng: true,
 jpegRecompress: false,
 jpegoptim: true,
 mozjpeg: true,
 gifsicle: true,
 svgo: true,
 concurrent: 10
 }))
 .pipe(gulp.dest('dist'));
 });*/

// 清理
/*gulp.task('clean', function() {
 return gulp.src(['dist/img'], {read: false})
 .pipe(clean());
 });

 // 预设任务
 gulp.task('default', ['clean'], function() {
 gulp.start('img');
 });*/


