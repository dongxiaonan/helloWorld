/*global module:false*/
module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    // Metadata.
    pkg: grunt.file.readJSON('package.json'),
    jshint: {
      gruntfile:{
	    src: 'Gruntfile'
      },
      all: [
	    'src/main/webapp/scripts/js/*.js',
	    'src/test/js/spec/*.js'
      ],	
      options: {
	    jshintrc: '.jshintrc'
      }
    },
    jasmine: {
      src: 'src/main/webapp/scripts/js/*.js',
      options: {
	    specs: 'src/test/js/spec/*.js',
	    vendor: 'src/main/webapp/scripts/lib/**/*.js'
      }
    },
    watch: {
      gruntfile: {
        files: '<%= jshint.gruntfile.src %>',
        tasks: ['jshint:gruntfile']
      },
      lib_test: {
        files: '<%= jshint.all %>',
        tasks: ['jshint:all', 'jasmine']
      }
    }
  });

  // These plugins provide necessary tasks.
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-contrib-jasmine');

  // Default task.
  grunt.registerTask('test', ['jshint', 'jasmine']);
  grunt.registerTask('default', ['test', 'watch']);

};
